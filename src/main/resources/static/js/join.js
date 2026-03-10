const form = document.getElementById("joinForm");
const result = document.getElementById("result");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const username = document.getElementById("username").value;

    try {
        const response = await fetch("http://localhost:8080/auth/join", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username
            })
        });

        const data = await response.json();

        if (response.ok) {
            result.textContent = "User created!";
        } else {
            result.textContent = data.message || "Failed to create user";
        }

    } catch (error) {
        result.textContent = "Server connection error";
    }
});