function getTeacherPosts(){
    const userIdTeacher = localStorage.getItem('userIdTeacher');
    const urlTeacherPosts = `http://localhost:9999/api/post/teacherPosts/${userIdTeacher}`;
    const token = localStorage.getItem('token');
    const container = document.getElementById("blog-container");
    const videoElement = document.querySelector("video");
    const sourceElement = videoElement.querySelector("source");
    const titleElement = document.querySelector("h2");
    fetch(urlTeacherPosts, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        console.log("data: \n", data);
        if(data.result.length <= 0){
            const div = document.createElement("div");
            div.innerHTML = `<h3 class="text-base font-semibold text-gray-800"> chưa có bài giảng nào </h3>`;
            container.appendChild(div);
        }
        else{
            data.result.forEach(video => {
            const div = document.createElement("div");
            div.className = "bg-gray-100 p-4 rounded-lg hover:bg-indigo-100 cursor-pointer transition";
            div.innerHTML = `<h3 class="text-base font-semibold text-gray-800">${video.title}</h3>`;
            div.onclick = () => {
                sourceElement.src = video.video;
                videoElement.load(); 
                titleElement.textContent = `Bài giảng: ${video.title}`;
            };
            container.appendChild(div);
            });
        }
    })
    .catch((err) => {
        console.log("error when get teacher posts: \n", err);
    })
}

window.addEventListener("DOMContentLoaded", () => {
    const userIdTeacher = localStorage.getItem('userIdTeacher');
    console.log("userid teacher:\n ", userIdTeacher);
    getTeacherPosts();
});