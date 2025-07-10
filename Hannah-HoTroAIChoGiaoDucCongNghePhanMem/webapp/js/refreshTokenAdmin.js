setInterval(() => {
    const tokenAdmin = localStorage.getItem('tokenAdmin');
    console.log("token valid");
    const urlCheckTokenAdmin = "http://localhost:9999/api/identity/auth/checkToken";
    const urlRefreshTokenAdmin = "http://localhost:9999/api/identity/auth/refreshToken";
    const objectTokenAdmin = {
        "token": tokenAdmin
    };
    fetch(urlCheckTokenAdmin, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(objectTokenAdmin) 
    })
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        if(!data.result.valid){
            fetch(urlRefreshTokenAdmin, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(objectTokenAdmin)
            })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log("new token after has been refresh tokenAdmin: ", data.result.token);
                localStorage.removeItem('tokenAdmin');
                localStorage.setItem('tokenAdmin', data.result.token);
            })
            .catch((err) => {
                console.log("error when refresh tokenAdmin: ", err);
                localStorage.clear();
                alert('vui long dang nhap lai');
                window.location.href = "loginAdmin.html";
            })
        }
    })
    .catch((err) => {
        console.log("error when check tokenAdmin: ", err);
        alert('vui long dang nhap lai');
        window.location.href = "loginAdmin.html";
    })

}, 10 * 1000);