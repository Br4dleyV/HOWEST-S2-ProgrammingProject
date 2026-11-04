import * as API from './api.js';
import * as LocalStorageAbstractor from "./data-connector/local-storage-abstractor.js";

const outcome = document.querySelector('#battleStatus');
const leaderboard = document.querySelector('#scoreboard ol');

function init() {
    const playerToken = LocalStorageAbstractor.loadFromStorage('playerToken'); // remove quotes

    API.getGameInfo(LocalStorageAbstractor.loadFromStorage('gameId'))
        .then((gameInfo) => {
            gameInfo = gameInfo.game;

            gameInfo.players.sort((a, b) => b.totalPrestigePoints - a.totalPrestigePoints); // sort by totalPrestigePoints
            leaderboard.innerHTML = ""; // clear previous leaderboard
            for (let i = 0; i < gameInfo.players.length; i++) {                
                gameInfo.players[i].playerToken = gameInfo.players[i].playerToken.replace(/"/g, ""); // remove quotes
                leaderboard.insertAdjacentHTML('beforeend', `<li>${i + 1} - ${gameInfo.players[i].name} - ${gameInfo.players[i].totalPrestigePoints}</li>`);
            }

            // check if you are the winner or loser
            if (!gameInfo.winner) { 
                outcome.innerHTML = `<h1>DRAW!</h1>`;
            } else if (gameInfo.winner.playerToken === playerToken) {
                outcome.innerHTML = `<h1>YOU WON!</h1>`;
            } else {
                outcome.innerHTML = `<h1>YOU LOST!</h1>`;
            }
        })
        .catch((error) => {
            console.error('Error fetching game info:', error);
        });
}

init();