let page = 1;
let size = 3;
let isLoading = false;
let totalPage = 0;
let noMorePostsShown = false; 

function getMyBlogs() {
    isLoading = true;
    const token = localStorage.getItem('token');
    const urlMyPosts = `http://localhost:9999/api/post/myPosts?page=${page}&size=${size}`;

    fetch(urlMyPosts, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
    })
    .then((response) => response.json())
    .then((data) => {
        totalPage = data.result.totalPage;
        console.log('page: ', page);

        const blogContainer = document.getElementById("blog-container");

        if (data.result.data.length === 0 && !noMorePostsShown) {
            const div = document.createElement("div");
            div.innerHTML = `<h3 class="text-base font-semibold text-gray-800 text-center mt-4"> Hết bài viết</h3>`;
            blogContainer.appendChild(div);
            noMorePostsShown = true;
            return;
        }

        data.result.data.forEach(post => {
            const article = document.createElement("article");
            article.className = "mb-8 p-4 border border-gray-300 rounded-lg shadow-sm hover:shadow-md transition-shadow";

            article.innerHTML = `
                <div class="flex items-center mb-3 space-x-3">
                    <div class="rounded-full w-10 h-10 overflow-hidden">
                        <img src="http://localhost:8083/file/media/download/930f20a4-73b0-4bd0-b3aa-ffa68bdc7e4b.jpg" alt="Avatar" class="w-full h-full object-cover rounded-full">
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

        page++;
        isLoading = false;
    })
    .catch((err) => {
        console.log("error when get my posts: ", err);
        isLoading = false;
    });
}

function createPost() {
    const urlCreatePost = "http://localhost:9999/api/post/createPost";
    const submitBtn = document.getElementById('submitPost');

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
});

window.addEventListener("scroll", () => {
    if (
        !isLoading &&
        page <= totalPage &&
        window.innerHeight + window.scrollY >= document.body.offsetHeight - 300
    ) {
        getMyBlogs();
    }
});
