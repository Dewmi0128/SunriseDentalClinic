const API_BASE_URL = "http://localhost:8080/api";

document
    .getElementById("loginForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const username =
            document.getElementById("username").value.trim();

        const password =
            document.getElementById("password").value;

        const message =
            document.getElementById("loginMessage");

        message.textContent = "Logging in...";
        message.style.color = "#333";

        try {

            const response = await fetch(
                `${API_BASE_URL}/auth/login`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        username: username,
                        password: password
                    })
                }
            );

            const result = await response.text();

            if (response.ok) {

                message.textContent = result;
                message.style.color = "green";

                setTimeout(function () {
                    window.location.href = "dashboard.html";
                }, 1000);

            } else {

                message.textContent = result;
                message.style.color = "red";
            }

        } catch (error) {

            console.error("Login error:", error);

            message.textContent =
                "Unable to connect to the server.";

            message.style.color = "red";
        }
    });