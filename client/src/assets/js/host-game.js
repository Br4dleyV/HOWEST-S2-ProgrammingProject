import * as ErrorHandler from "./data-connector/error-handler.js";
import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";
import * as API from './api.js';

const incrementPlayers = document.querySelector("#increment");
const decrementPlayers = document.querySelector("#decrement");
const numOfPlayers = document.querySelector("#num-of-players");
const hostGameForm = document.querySelector("#host-game-form");
const gameName = document.querySelector("#game-name");

function hostGameFunctions() {
    incrementPlayers.addEventListener("click", increment);
    decrementPlayers.addEventListener("click", decrement);
    numOfPlayers.addEventListener("input", validatePlayers);
    hostGameForm.addEventListener("submit", hostGame);
}

function increment() {
    const currentValue = parseInt(numOfPlayers.value, 10);
    if (currentValue < 4) {
        numOfPlayers.value = currentValue + 1;
    }
}

function decrement() {
    const currentValue = parseInt(numOfPlayers.value, 10);
    if (currentValue > 2) {
        numOfPlayers.value = currentValue - 1;
    }
}

function validatePlayers() {
    let value = parseInt(numOfPlayers.value, 10);
    if (isNaN(value) || value < 2) {
        value = 2;
    } else if (value > 4) {
        value = 4;
    } else {
        // No action needed if the value is valid
    }
    numOfPlayers.value = value;
}

// Handles the form submission for hosting a game
function hostGame(e) {
    e.preventDefault();

    const { strGameName, playerCount } = getFormValues();
    if (!strGameName) return;

    API.createGameOnServer(strGameName, playerCount).then(res => {
        saveGameDetailsToLocalStorage(res);
        redirectToLobby();
    }).catch(error => {
        ErrorHandler.handleError(error);
    });
}

function getFormValues() {
    const strGameName = gameName.value.trim().replace(/\s+/g, "_"); // Replace spaces with underscores
    const playerCount = parseInt(numOfPlayers.value, 10);

    return { strGameName, playerCount };
}

function saveGameDetailsToLocalStorage(response) {
    LocalStorageAbstractor.saveToStorage("gameId", response.game.gameId);
    LocalStorageAbstractor.saveToStorage("playerName", response.game.gameOwner.name);
    LocalStorageAbstractor.saveToStorage("playerToken", response.game.gameOwner.playerToken);
}

function redirectToLobby() {
    window.location.href = "./lobby-screen.html";
}

export { hostGameFunctions };