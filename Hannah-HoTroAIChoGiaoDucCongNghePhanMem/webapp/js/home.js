
function checkUserNewOrNot(){
    const token = localStorage.getItem('token');
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64);
    const payload = JSON.parse(payloadJson);
    console.log("payload: ", payload);
    let userId = payload.sub;
    console.log("userId: ", userId);

    const urlGetProfileFromUserId = `http://localhost:9999/api/profile/profiles/getProfile/fromUserId/${userId}`;
    fetch(urlGetProfileFromUserId, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then((response) => {
        return response.json()
    })
    .then((data) => {
        console.log(data);
        localStorage.setItem('profileId', data.result.id);
        localStorage.setItem('gender', data.result.gender);
        if(data.result.fullName == null){
            console.log(data.result.fullName);
            alert('vì đây là lần đầu bạn login vào web, nên vui lòng thực hiện thêm thông tin')
            window.location.href = "UpdateProfileNewUser.html";        
        }
        let user = JSON.parse(localStorage.getItem('user'));
        if(user == null){
            user = {
                "id": data.result.id,
                "userId": data.result.userId,
                "avatar": data.result.avatar,
                "fullName": data.result.fullName,
                "dob": data.result.dob,
                "gender": data.result.gender,
                "address": data.result.address,
                "phoneNumber": data.result.phoneNumber
            };
            localStorage.setItem('user', JSON.stringify(user));
        }
        console.log('user: ', user);
        

    })
    .catch((err) => {
        console.log('error when get profile from user id ', err);
    })
}

function checkUserHasBeenLogin(){
    const authButtons = document.getElementById("auth-buttons");
    const token = localStorage.getItem("token");
    if(token){
        authButtons.innerHTML = `
        <button
            class="bg-red-600 text-white rounded-md px-4 py-1 font-semibold hover:bg-red-700 transition"
            type="button"
            id="logoutBtn"
        >
            Log out
        </button>
        `;
        const logoutBtn = document.getElementById("logoutBtn");
        logoutBtn.addEventListener("click", () => {
        localStorage.clear();
        window.location.href = "index.html";
        });
    }
    else{
        authButtons.innerHTML = `
        <button
            class="border border-indigo-600 text-indigo-600 rounded-md px-4 py-1 font-semibold hover:bg-indigo-600 hover:text-white transition"
            type="button"
        >
            <a href="login.html">log in</a>
        </button>
        <button
            class="bg-indigo-600 text-white rounded-md px-4 py-1 font-semibold hover:bg-indigo-700 transition"
            type="button"
        >
            <a href="signup.html">sign up</a>
        </button>
        `
    }
}

let pageProfileTeacher = 0;
let sizeProfileTeacher = 6;
let isLoadingProfileTeacher = false;

function userRoleTeacher() {
    isLoadingProfileTeacher = true;
    const urlUserRoleTeacher = `http://localhost:9999/api/identity/users/getUserRole/TEACHER?page=${pageProfileTeacher}&size=${sizeProfileTeacher}`;

    fetch(urlUserRoleTeacher, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then((response) => response.json())
    .then((data) => {
        const frameTeacher = document.getElementById('frameTeacher');
        console.log(data);

        const token = localStorage.getItem('token');

        data.result.data.forEach(profile => {
            const urlTeacherPostsInHome = `http://localhost:9999/api/post/teacherPosts/${profile.userId}`;

            fetch(urlTeacherPostsInHome, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            })
            .then((response) => response.json())
            .then((courseData) => {
                const countCourses = courseData.result.length;

                const article = document.createElement("article");
                article.className = "flex flex-col bg-gray-50 rounded-lg p-5 shadow hover:shadow-md transition-shadow h-full";
                article.style.minHeight = "220px";

                article.innerHTML = `
                    <div class="flex items-center space-x-4 mb-4">
                        <div class="rounded-full w-14 h-14 flex items-center justify-center font-semibold text-indigo-600 bg-indigo-100">
                            <img src="${profile.avatar}" alt="Avatar" class="w-full h-full object-cover rounded-full">
                        </div>
                        <div>
                            <h2 class="font-semibold text-lg text-gray-900 leading-tight">${profile.fullName}</h2>
                            <p class="text-gray-600 text-sm leading-relaxed">
                                address: ${profile.address} / phoneNumber: ${profile.phoneNumber}
                            </p>
                        </div>
                    </div>
                    <div class="flex items-center justify-between mt-auto">
                        <div class="border border-indigo-600 rounded-md px-3 py-1 font-semibold text-indigo-600 bg-indigo-100">
                            ${countCourses} Bài
                        </div>
                        <button
                            data-user-id="${profile.userId}"
                            class="join-btn border border-indigo-600 rounded-md px-6 py-2 text-indigo-600 font-semibold hover:bg-indigo-600 hover:text-white transition"
                            type="button"
                        >
                            vào học
                        </button>
                    </div>
                `;

                frameTeacher.appendChild(article);
            })
            .catch((err) => {
                console.error("Error when fetching teacher posts:", err);
            });
        });

        isLoadingProfileTeacher = false;
        pageProfileTeacher++;
    })
    .catch((err) => {
        console.error("Error when fetching teacher profiles:", err);
        alert("Lỗi khi lấy danh sách giáo viên.");
    });
}


function getUserIdTeacher(){
     document.getElementById('frameTeacher').addEventListener('click', function (event) {
        if (event.target.classList.contains('join-btn')) {
            const userId = event.target.dataset.userId;
            console.log("User ID:", userId);
            localStorage.setItem('userIdTeacher', userId);
            window.location.href = "knowledge.html";
        }
    });
}

let page = 1;
const size = 3;
let isLoading = false;
let hasMore = true;


function blogStudents() {
    if (isLoading || !hasMore) return;
    isLoading = true;

    console.log("page: ", page);

    const urlAllPosts = `http://localhost:9999/api/post/getAllPost?page=${page}&size=${size}`;
    fetch(urlAllPosts, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then((response) => response.json())
    .then(async (data) => {
        const blogStudent = document.getElementById('frameBlogStudent');
        const posts = data.result.data;

        if (posts.length === 0) {
            hasMore = false;
            return;
        }

        // ⚠️ Không append bài viết ngay — chờ fetch avatar xong đã
        const articles = await Promise.all(
            posts.map(async (post) => {
                const userIdPerson = post.userId;
                const urlProfileFromUserId = `http://localhost:9999/api/profile/profiles/getProfile/fromUserId/${userIdPerson}`;

                let avt = "/default-avatar.png";
                try {
                    const response = await fetch(urlProfileFromUserId, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json"
                        }
                    });
                    const profileData = await response.json();
                    avt = profileData.result?.avatar ?? avt;
                } catch (err) {
                    console.log("Lỗi khi lấy avatar từ userId:", err);
                }

                const article = document.createElement('article');
                article.className = "mb-8 p-4 border border-gray-300 rounded-lg shadow-sm hover:shadow-md transition-shadow";
                article.innerHTML = `
                    <div class="flex items-center mb-3 space-x-3">
                        <div class="rounded-full w-10 h-10 overflow-hidden">
                            <img src="${avt}" alt="Avatar" class="w-full h-full object-cover rounded-full">
                        </div>
                        <span class="font-semibold text-gray-900 text-lg select-text">${post.fullName}</span>
                        <span class="text-xs text-gray-500">${post.createdTime}</span>
                    </div>
                    <div class="border border-gray-200 rounded-md p-3 text-sm text-gray-700 leading-relaxed whitespace-pre-wrap bg-gray-50">
                        ${post.content}
                    </div>
                `;
                return article; // Trả về element, chưa append
            })
        );

        // ✅ Sau khi tất cả avatar đã được lấy → append lần lượt đúng thứ tự
        articles.forEach(article => blogStudent.appendChild(article));

        page++;
    })
    .catch((err) => {
        console.log("Lỗi khi lấy danh sách bài viết:", err);
    })
    .finally(() => {
        isLoading = false;
    });
}

function isRole (){
    const token = localStorage.getItem('token');
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64);
    const payload = JSON.parse(payloadJson);
    console.log("payload is role: ", payload);
    const role =  payload.scope;
    console.log("role: ", role);
    if(role === "TEACHER"){
        const printUploadFile = document.getElementById('printUploadFile');
        console.log("print upload: ", printUploadFile);
        printUploadFile.classList.remove('hidden');
    }
}


window.addEventListener("DOMContentLoaded", () => {
    console.log("token: ", localStorage.getItem("token"));
    checkUserHasBeenLogin();
    isRole();
    if (window.location.pathname.endsWith("index.html")) {
        console.log("Bạn đang ở trang index.html");
        blogStudents();
        userRoleTeacher();
        checkUserNewOrNot();
        getUserIdTeacher();
    }
    const blogStudentFrame = document.getElementById('frameBlogStudent');
    blogStudentFrame.addEventListener("scroll", () => {
    const scrollTop = blogStudentFrame.scrollTop;
    const scrollHeight = blogStudentFrame.scrollHeight;
    const clientHeight = blogStudentFrame.clientHeight;

    if (scrollTop + clientHeight >= scrollHeight - 100) {
        blogStudents();
    }
});
})


