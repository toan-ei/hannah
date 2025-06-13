function updateAvatar(){
    const urlUpdateAvatar = 'http://localhost:9999/api/profile/profiles/avatar';
    
    let user = JSON.parse(localStorage.getItem('user')); 
    console.log(user);
    const form = document.getElementById('updateForm');
    form.addEventListener('submit', async function (e) {
        e.preventDefault(); // Ngăn reload trang
        const token = localStorage.getItem('token');
        console.log(token);
        const avatarInput = document.getElementById("avatar");
        const file = avatarInput.files[0];
        const formData = new FormData();
        formData.append("media", file);

        try {
            const response = await fetch(urlUpdateAvatar, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData
            });

            if (!response.ok) {
            const errorData = await response.json();
            console.error('Upload lỗi:', errorData);
            alert('Cập nhật thất bại!');
            return;
            }
            const result = await response.json();
            user.avatar = result.result.url;
            localStorage.setItem('user', JSON.stringify(user));
            console.log('Thành công:', result);
            alert('Cập nhật ảnh đại diện thành công!');
            window.location.href = "index.html";
        } catch (error) {
            console.error('Lỗi mạng hoặc backend:', error);
            alert('Có lỗi xảy ra. Vui lòng thử lại sau.');
        }
    });
}

window.addEventListener("DOMContentLoaded", () => {
    updateAvatar();
})