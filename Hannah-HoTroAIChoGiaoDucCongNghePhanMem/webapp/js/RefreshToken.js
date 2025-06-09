setInterval(() => {
    const token = localStorage.getItem('token');
    console.log("token valid");
    const urlCheckToken = "http://localhost:9999/api/identity/auth/checkToken";
    const urlRefreshToken = "http://localhost:9999/api/identity/auth/refreshToken";
    const objectToken = {
        "token": token
    };
    fetch(urlCheckToken, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(objectToken) 
    })
    .then((response) => {
        return response.json();
    })
    .then((data) => {
        if(!data.result.valid){
            fetch(urlRefreshToken, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(objectToken)
            })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log("new token after has been refresh token: ", data.result.token);
                localStorage.removeItem('token');
                localStorage.setItem('token', data.result.token);
            })
            .catch((err) => {
                console.log("error when refresh token: ", err);
                localStorage.clear();
                alert('vui long dang nhap lai');
                window.location.href = "login.html";
            })
        }
    })
    .catch((err) => {
        console.log("error when check token: ", err);
    })

}, 6 * 60 * 1000);