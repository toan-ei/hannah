const profileId = localStorage.getItem('profileId');
console.log('profileId: ', profileId);
const urlUpdateProfile = `http://localhost:9999/api/profile/profiles/updateProfile/${profileId}`;
let updateBtn = document.getElementById("update");
updateBtn.addEventListener('click', (event) => {
    event.preventDefault();
    let fullName = document.getElementById("fullName").value;
    let dob = document.getElementById("dob").value;
    console.log(dob);
    let gender = localStorage.getItem('gender');
    let address = document.getElementById("address").value;
    let phoneNumber = document.getElementById("phoneNumber").value;
    if(!fullName || !dob || !address || !phoneNumber){
        alert('vui lòng nhập đầy đủ thông tin');
        return;
    }
    let dataUpdateProfile = {
        "fullName": fullName,
        "dob": dob,
        "gender": gender,
        "address": address,
        "phoneNumber": phoneNumber
    };
    let token = localStorage.getItem("token");
    fetch(urlUpdateProfile, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dataUpdateProfile)
    })
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        let user = {
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
        localStorage.removeItem('profileId');
        localStorage.removeItem('gender');
        alert('update success');
        window.location.href = "UpdateAvatarNewUser.html";
    })
    .catch((err) => {
        console.log('error when update profile user: ', err);
    })
})
const skip = document.getElementById('skip');
skip.onclick = () => {
    localStorage.clear();
    window.location.href = "index.html";
}