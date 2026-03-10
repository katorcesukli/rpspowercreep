const gamesList = document.getElementById("gamesList");

    async function refreshGames() {
        const res = await fetch('/api/game/waiting');
        const games = await res.json();

        gamesList.innerHTML = "";
        games.forEach(game => {
            const li = document.createElement('li');
            li.textContent = `Game ID: ${game.id} | Player1: ${game.player1.name}`;
            const joinBtn = document.createElement('button');
            joinBtn.textContent = "Join";
            joinBtn.onclick = () => joinGame(game.id);
            li.appendChild(joinBtn);
            gamesList.appendChild(li);
        });
    }

    async function joinGame(gameId) {
        const playerName = document.getElementById("playerName").value;
        if (!playerName) {
            alert("Enter your name first!");
            return;
        }

        const res = await fetch(`/api/game/join?gameId=${gameId}&playerName=${playerName}`, {
            method: 'POST'
        });
        const game = await res.json();
        alert(`Joined game ${game.id} as ${playerName}`);
        // Redirect to game page
        localStorage.setItem('gameId', game.id);
        localStorage.setItem('playerName', playerName);
        window.location.href = 'game.html';
    }

    // Initial refresh
    refreshGames();