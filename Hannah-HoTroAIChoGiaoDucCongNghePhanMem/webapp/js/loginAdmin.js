const urlGetTokenAdmin = "http://localhost:9999/api/identity/auth/token";
let usernameInputAdmin = document.querySelector(".username");
let passwordInputAdmin = document.querySelector(".password");
let sendBtn = document.querySelector(".send");
sendBtn.addEventListener("click", () => {
    let usernameAdmin = usernameInputAdmin.value;
    let passwordAdmin = passwordInputAdmin.value;
    if(usernameAdmin == null || passwordAdmin == null){
        alert('vui lòng điền đầy đủ thông tin');
        return;
    }
    let data = {
        "username": usernameAdmin,
        "password": passwordAdmin,
    }
    fetch(urlGetTokenAdmin, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then((data) => {
        return data.json();
    })
    .then((response) => {
        alert("ban da dang nhap thanh cong");
        localStorage.setItem("tokenAdmin", response.result.token);
        window.location.href = "admin.html";
    })
    .catch((err) => {
        console.log("loi dang ki: ", err);
    })
})