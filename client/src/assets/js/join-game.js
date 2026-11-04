import * as ErrorHandler from "./data-connector/error-handler.js";
import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";
import * as API from "./api.js";
import { showModal } from "./util/modal.js";

const gameTemplate = document.querySelector("#game-list-template");
const gameList = document.querySelector("#game-list");

function refreshGames() {
    getSessions();
    document.querySelector("#refresh-games-btn").addEventListener("click", getSessions);
}

function getSessions() {
    API.getGames().then(res => {
        clearGameList();
        const sortedGames = sortGames(res);
        renderGames(sortedGames);
    }).catch(error => {
        ErrorHandler.handleError(error);
    });
}

function clearGameList() {
    gameList.innerHTML = ""; // Clear the game list before rendering new games
}

function sortGames(games) {
    return Object.values(games).sort((a, b) => {
        const freeSpotsA = a.numberOfPlayers - a.players.length;
        const freeSpotsB = b.numberOfPlayers - b.players.length;

        // Sort by free spots (descending)
        if (freeSpotsB !== freeSpotsA) {
            return freeSpotsB - freeSpotsA;
        }

        // If free spots are the same, sort by the number of players (ascending)
        return a.players.length - b.players.length;
    });
}

function renderGames(games) {
    games.forEach(game => {
        const template = gameTemplate.content.cloneNode(true);
        const gameButton = template.querySelector(".game-button");
        const playerBox = template.querySelector(".player-box");
        const gameState = template.querySelector(".game-state");

        // Set game details
        gameButton.textContent = game.gameName;
        playerBox.textContent = `${game.players.length}/${game.numberOfPlayers}`;
        gameButton.dataset.gameId = game.gameId; // Attach gameId to the button

        // Set game state and attach event listener
        if (game.started) {
            gameState.textContent = "Started | Spectate";
        } else {
            gameState.textContent = "Waiting for Players";
            gameButton.addEventListener("click", handleJoinGameClick); // Use a handler function
        }

        // Append the game to the list
        gameList.appendChild(template);
    });
}

function handleJoinGameClick(event) {
    const gameId = event.target.dataset.gameId; // Retrieve gameId from the button
    if (!gameId) {
        console.error("Game ID is missing.");
        return;
    }

    joinGame(gameId); // Pass the gameID
}

function joinGame(gameId) {
    LocalStorageAbstractor.saveToStorage("gameId", gameId);

    API.joinGame(gameId).then(res => {
        savePlayerData(res);
        redirectToLobby();
    }).catch(error => {
        showModal({
            title: "Could not join game",
            body: error.cause,
            buttons: [{ text: "OK", value: "ok" }]
        });
        ErrorHandler.handleError(error);
    });
}

function savePlayerData(res) {
    LocalStorageAbstractor.saveToStorage("playerToken", res.playerToken);
}

function redirectToLobby() {
    window.location.href = "lobby-screen.html";
}

export { refreshGames, joinGame };