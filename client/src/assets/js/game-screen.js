import * as API from './api.js';
import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";
import * as ErrorHandler from "./data-connector/error-handler.js";
import { toggleGemEventListeners, tokenFirstStep } from "./game-screen/taking-token-logic.js";
import { renderFullBoard } from './game-screen/renderer.js';
import { toggleDevelopmentEventListeners} from './game-screen/buying-card-logic.js';
import { toggleReserveEventListeners} from './game-screen/reserving-cards-logic.js';
import { toggleNobleEventListeners } from './game-screen/taking-noble-logic.js';

import { showModal } from './util/modal.js';

const timeoutTime = 2000; // Wait 2 seconds before checking game status again
const pointsRequired = 15; // Points required to win

function init() {
    renderFullBoard()
        .then(() => {
            gameLoop(); // call after render, ensuring DOM is ready
        })
        .catch((err) => console.error("Error rendering board:", err));
}

function gameLoop() {
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");
    const playerName = LocalStorageAbstractor.loadFromStorage("playerName");

    API.getGameInfo(gameId)
        .then((gameInfo) => {
            gameInfo = gameInfo.game;
            checkIfPlayerHas15Points(gameInfo); // Check if someone has 15 points
            if (gameInfo.winner) return endGame(gameInfo.winner); // Check if the game is over
            // Check if it's the player's turn
            if (gameInfo.currentPlayer.name === playerName) {
                renderFullBoard().then(() => {
                    highlightPlayer(playerName, playerName, gameInfo);
                    applyTurnIndicator(playerName);
                    toggleEventListeners(true);
                    return showModal({ title: "It's your turn!", buttons: [{ text: "OK", value: "close" }]});
                });
            } else {
                // Not the player's turn, setTimeout to check again
                highlightPlayer(gameInfo.currentPlayer.name, playerName, gameInfo);
                applyTurnIndicator(gameInfo.currentPlayer.name);
                toggleEventListeners(false);

                setTimeout(gameLoop, timeoutTime);
            }
        })
        .catch(ErrorHandler.handleError);
}

// Renders the 'waiting for player X' or 'it's your turn' message
function highlightPlayer(currentPlayerName, localPlayerName, gameInfo) {
    const statusEl = document.querySelector("#game-status");

    if (!statusEl) {
        console.error("Status element not found");
        return;
    }

    if (currentPlayerName === localPlayerName) {
        statusEl.textContent = "It's your turn!";
    } else {
        statusEl.textContent = `Waiting for ${currentPlayerName}`;
    }
}

// Checks if it's the player's turn and enables/disables event listeners accordingly
function toggleEventListeners(isMyTurn) {
    toggleDevelopmentEventListeners(isMyTurn);
    toggleGemEventListeners(tokenFirstStep, isMyTurn);
    toggleReserveEventListeners(isMyTurn);
    toggleNobleEventListeners(isMyTurn);
}

// Function to end the game and show the winner
function endGame(winner) {
    // Alert player that the game is over
    showModal({
        title: "Panos Boss",
        body: `Game over! The winner is ${winner.name}! Click OK to continue to game overview.`,
        buttons: [{ text: "OK", value: "ok" }]
    });

    // Send the player to the end screen
    window.location.href = 'end-screen.html';
}

// Function to check if the player has 15 points, and alert player if so
function checkIfPlayerHas15Points(gameInfo) {
    const playerName = LocalStorageAbstractor.loadFromStorage("playerName");

    gameInfo.players.forEach(player => {
        if (player.points >= pointsRequired) {
            if (player.name === playerName) {
                // Show a modal if the player has 15 points
                showModal({
                    title: "Heads up!",
                    body: `You have reached 15 points!`,
                    buttons: [{ text: "OK", value: "ok" }]
                });
            } else {
                // Alert other players if someone else has 15 points
                showModal({
                    title: "Heads up!",
                    body: `${player.name} has reached 15 points!`,
                    buttons: [{ text: "OK", value: "ok" }]
                });
            }
        }
    });
}

// Shows a yellow border around the current player for the player whose turn it is
function applyTurnIndicator(currentPlayerName) {
    const players = document.querySelectorAll(".player");
    const playerName = LocalStorageAbstractor.loadFromStorage("playerName");

    players.forEach(playerEl => {
        const nameEl = playerEl.querySelector(".player-name");

        let screenName = nameEl.textContent.trim();

        if (screenName === "You") {
            screenName = playerName;
        }

        if (screenName === currentPlayerName) {
            setTimeout(() => {
                playerEl.classList.add("turn-indicator");
            }, 10);
        }
    });
}

init();
export { gameLoop };