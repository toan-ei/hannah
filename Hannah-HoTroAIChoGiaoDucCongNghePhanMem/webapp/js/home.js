function checkUserNewOrNot(){
    const token = localStorage.getItem('token');
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64); // base64 decode
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
            alert('sss')
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
}

window.addEventListener("DOMContentLoaded", () => {
    console.log("token: ", localStorage.getItem("token"));
    checkUserNewOrNot();
    checkUserHasBeenLogin();
})