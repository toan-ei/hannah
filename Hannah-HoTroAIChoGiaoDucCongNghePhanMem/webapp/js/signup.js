const urlCreateUser = "http://localhost:9999/api/identity/users/createUser";
let usernameInput = document.querySelector(".username");
let passwordInput = document.querySelector(".password");
let genderInput = document.querySelector('.gender');
let sendBtn = document.querySelector(".send");
sendBtn.addEventListener("click", () => {
    let username = usernameInput.value;
    let password = passwordInput.value;
    let gender = genderInput.value;
    if(username == null || password == null, gender == null){
        alert('vui lòng điền đầy đủ thông tin')
        return;
    }
    let data = {
        "username": username,
        "password": password,
        "gender": gender
    }
    fetch(urlCreateUser, {
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
        alert("ban da dang ki thanh cong");
        window.location.href = "login.html";
    })
    .catch((err) => {
        console.log("loi dang ki: ", err);
    })
})
