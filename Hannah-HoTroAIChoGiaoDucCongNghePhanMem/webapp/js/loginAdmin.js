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
        const token = response.result.token;
        const payloadBase64 = token.split('.')[1];
        const payloadJson = atob(payloadBase64);
        const payload = JSON.parse(payloadJson);
        console.log("payload is role: ", payload);
        const role =  payload.scope;
        console.log("role: ", role);
        if(role === "ADMIN"){
            alert("ban da dang nhap thanh cong");
            localStorage.setItem("tokenAdmin", response.result.token);
            window.location.href = "admin.html";
        }
        else{
            alert("vui lòng đăng nhập đúng tài khoản admin");
        }
    })
    .catch((err) => {
        console.log("loi dang ki: ", err);
    })
})