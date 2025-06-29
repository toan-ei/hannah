let pageMyBlog = 1;
let sizeMyBlog = 3;
let isLoadingMyBlog = false;
let totalPageMyBlog = 0;
let noMorePostsShown = false;

function getMyBlogs() {
    isLoadingMyBlog = true;
    const token = localStorage.getItem('token');
    const urlMyPosts = `http://localhost:9999/api/post/myPosts?page=${pageMyBlog}&size=${sizeMyBlog}`;

    fetch(urlMyPosts, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
    })
    .then((response) => response.json())
    .then(async (data) => {
        totalPageMyBlog = data.result.totalPage;
        console.log('page: ', pageMyBlog);

        const blogContainer = document.getElementById("blog-container");
        const posts = data.result.data;

        if (posts.length === 0 && !noMorePostsShown) {
            const div = document.createElement("div");
            div.innerHTML = `<h3 class="text-base font-semibold text-gray-800 text-center mt-4"> Hết bài viết</h3>`;
            blogContainer.appendChild(div);
            noMorePostsShown = true;
            return;
        }

        const postPromises = posts.map(async (post) => {
            const userIdPerson = post.userId;
            const urlProfileFromUserId = `http://localhost:9999/api/profile/profiles/getProfile/fromUserId/${userIdPerson}`;

            let avt = null;
            try {
                const response = await fetch(urlProfileFromUserId, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
                const profileData = await response.json();
                avt = profileData.result.avatar;
            } catch (err) {
                console.log("Error when getting profile from userId:", err);
            }

            const article = document.createElement("article");
            article.className = "mb-8 p-4 border border-gray-300 rounded-lg shadow-sm hover:shadow-md transition-shadow";

            article.innerHTML = `
                <div class="flex items-center mb-3 space-x-3">
                    <div class="rounded-full w-10 h-10 overflow-hidden">
                        <img src="${avt ?? '/default-avatar.png'}" alt="Avatar" class="w-full h-full object-cover rounded-full">
                    </div>
                    <span class="font-semibold text-gray-900 text-lg select-text">${post.fullName}</span>
                    <span class="text-xs text-gray-500">${post.createdTime}</span>
                </div>
                <div class="border border-gray-200 rounded-md p-3 text-sm text-gray-700 leading-relaxed whitespace-pre-wrap bg-gray-50">
                    ${post.content}
                </div>
            `;
            blogContainer.appendChild(article);
        });

        await Promise.all(postPromises);
        pageMyBlog++;
        isLoadingMyBlog = false;
    })
    .catch((err) => {
        console.log("error when get my posts: ", err);
        isLoadingMyBlog = false;
    });
}

function createPost() {
    const urlCreatePost = "http://localhost:9999/api/post/createPost";
    const submitBtn = document.getElementById('submitPost');
    if (!submitBtn) return;

    submitBtn.onclick = () => {
        const token = localStorage.getItem('token');
        const content = document.getElementById('contentPost').value.trim();
        const wordCount = content.split(/\s+/).filter(word => word.length > 0).length;

        if (wordCount > 200) {
            alert("Nội dung bài viết không được vượt quá 200 từ. Hiện tại bạn đang có " + wordCount + " từ.");
            return;
        }

        const dataContent = { content };

        fetch(urlCreatePost, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dataContent)
        })
        .then((response) => response.json())
        .then((data) => {
            alert("Bạn đã tạo bài viết thành công!");
            setTimeout(() => {
                window.location.href = "MyBlog.html";
            }, 1500);
        })
        .catch((err) => {
            console.log("error when creating blog", err);
        });
    };
}

window.addEventListener("DOMContentLoaded", () => {
    getMyBlogs();
    createPost();
    const blogContainer = document.getElementById("blog-container");
    blogContainer.addEventListener("scroll", () => {
        if (
            !isLoadingMyBlog &&
            pageMyBlog <= totalPageMyBlog &&
            blogContainer.scrollTop + blogContainer.clientHeight >= blogContainer.scrollHeight - 100
        ) {
            getMyBlogs();
        }
    });
});
