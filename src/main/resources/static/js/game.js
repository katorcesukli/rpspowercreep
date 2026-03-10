const joinForm = document.getElementById("joinForm");
const joinResult = document.getElementById("joinResult");
const gameSection = document.getElementById("gameSection");
const playerNameSpan = document.getElementById("playerName");
const gameIdInput = document.getElementById("gameId");
const choicesDiv = document.getElementById("choices");
const roundInfoDiv = document.getElementById("roundInfo");
const scoresDiv = document.getElementById("scores");

let username = null;
let gameId = null;
let playerId = null;

// --- Join Game ---
joinForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    username = document.getElementById("username").value;
    gameId = gameIdInput.value;

    if (!username || !gameId) {
        joinResult.textContent = "Enter your name and Game ID!";
        return;
    }

    try {
        const res = await fetch(`/api/game/join?gameId=${gameId}&playerName=${username}`, {
            method: "POST"
        });

        if (res.ok) {
            const game = await res.json();
            joinResult.textContent = `Joined game ${game.id} successfully!`;
            playerNameSpan.textContent = "Player: " + username;
            gameSection.style.display = "block";

            // Identify playerId for submitting moves
            if (game.player1.name === username) playerId = game.player1.id;
            else playerId = game.player2.id;

            loadGameState(); // initial load
        } else {
            joinResult.textContent = "Failed to join game. Check Game ID.";
        }
    } catch (err) {
        console.error(err);
        joinResult.textContent = "Server error";
    }
});

// --- Submit a move ---
async function submitMove(choice) {
    if (!playerId || !gameId) return;

    try {
        const res = await fetch(`/api/game/move?gameId=${gameId}&playerId=${playerId}&choice=${choice}`, {
            method: "POST"
        });
        const status = await res.json();

        if (status.waiting) {
            alert("Waiting for other player to submit...");
        } else {
            alert(`Round completed! Result: ${status.round?.result || '-'}`);
        }

        loadGameState(); // refresh after move
    } catch (err) {
        console.error(err);
        alert("Error submitting move");
    }
}

// --- Load / Refresh Game State ---
async function loadGameState() {
    if (!gameId) return;

    try {
        const res = await fetch(`/api/game/${gameId}`);
        const game = await res.json();

        // Update playerId if first load
        if (!playerId) {
            if (game.player1.name === username) playerId = game.player1.id;
            else if (game.player2) playerId = game.player2.id;
        }

        // Update scores
        scoresDiv.innerHTML = `
            ${game.player1.name}: ${game.player1.score || 0}<br>
            ${game.player2 ? game.player2.name : "-"}: ${game.player2?.score || 0}
        `;

        // Update round info
        const currentRoundNum = game.currentRound;
        const lastRound = game.rounds.find(r => r.roundNumber === currentRoundNum - 1);
        roundInfoDiv.innerHTML = `
            Round: ${currentRoundNum}<br>
            Player1: ${lastRound?.player1Choice || "-"} | Player2: ${lastRound?.player2Choice || "-"}<br>
            Result: ${lastRound?.result || "-"}
        `;

        // Update available choices dynamically
        const player = game.player1.name === username ? game.player1 : game.player2;
        choicesDiv.innerHTML = "Available moves: ";
        if (player.availableChoices) {
            player.availableChoices.forEach(c => {
                const btn = document.createElement("button");
                btn.textContent = c;
                btn.dataset.choice = c;
                btn.onclick = () => submitMove(c);
                choicesDiv.appendChild(btn);
            });
        }

    } catch (err) {
        console.error(err);
    }
}

// --- Poll game state every 3 seconds ---
setInterval(loadGameState, 3000);