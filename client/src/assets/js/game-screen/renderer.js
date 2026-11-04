import * as Helper from '../util/helpers.js';
import * as API from "../api.js";
import * as LocalStorageAbstractor from "../data-connector/local-storage-abstractor.js";
import { showModal } from "../util/modal.js";

const TOKENS_TO_DOUBLE = [];

// Function to render players in the game
function renderPlayers(gameInfo) {
    const playerTemplate = document.querySelector("#player-template");
    const playerPanel = document.querySelector("#players-panel");
    playerPanel.innerHTML = '';

    Object.entries(gameInfo.players).forEach(([index, player]) => {
        const playerName = player.name;
        const playerClone = playerTemplate.content.firstElementChild.cloneNode(true);
        if (player.name === LocalStorageAbstractor.loadFromStorage("playerName")) { // Check if player is the current player
            playerClone.querySelector(".player-name").textContent = `You`; // Player name comes here
        } else { // If not the current player 
            playerClone.querySelector(".player-name").textContent = player.name; // Player name comes here
        }
        playerClone.querySelector(".player-score").textContent = player.totalPrestigePoints; // Player score comes here
        playerPanel.appendChild(playerClone);

        const $showPlayerInfoButton = playerClone.querySelector("#show-player-info-button");
        $showPlayerInfoButton.dataset.playerName = playerName;
        $showPlayerInfoButton.addEventListener("click", () => {
            // Show player info when the button is clicked
            API.getGameInfo(gameInfo.gameId)
                .then((newGameInfo) => {
                    const playerInfo = getPlayerFromGameInfo(newGameInfo.game, playerName);
                    showPlayerInfo(playerInfo);
                });
        });
    });
}

// Function to get the player from gameInfo
function getPlayerFromGameInfo(gameInfo, playerName) {
    return gameInfo.players.find(player => player.name === playerName);
}

// Function to show player info in a modal
function showPlayerInfo(playerInfo) {
    if (!playerInfo) {
        showModal({
            title: "Player Info",
            body: "No player information available.",
            buttons: [
                { text: "OK", value: "ok" }
            ]
        });
    }

    // Count amount of reserved cards
    const reservedCardsCount = playerInfo.reserved.length;

    const developmentCards = playerInfo.bought.map(card => card.name).join(", ");
    const tokens = Object.entries(playerInfo.tokens.tokens).map(([gem, count]) => `${count}x ${gem.charAt(0).toUpperCase() + gem.slice(1).toLowerCase()}`).join(", ");

    showModal({
        title: `Player Info - ${playerInfo.name}`,
        body: `
                Prestige Points: ${playerInfo.totalPrestigePoints}
                <p>Tokens: ${tokens || "None"}</p>
                <p>Development Cards: ${developmentCards || "None"}</p>
                <p>Reserved Cards: ${reservedCardsCount}</p>
            `,
        buttons: [ { text: "OK", value: "ok" } ]
    });
}

// Function to render board
function renderDevelopmentCards(gameInfo) {
    const board = document.querySelector("#developments-panel");
    const levelTemplate = document.querySelector("#level-template");
    board.innerHTML = '';

    gameInfo.market.reverse(); // Reverse order of the levels so level 3 is on top
    gameInfo.market.forEach(level => {
        const levelClone = levelTemplate.content.firstElementChild.cloneNode(true);
        levelClone.querySelector("h3").textContent = `LEVEL ${level.level}`;

        const cardTemplate = levelClone.querySelector("#card-template");
        level.visibleCards.forEach(card => {
            let cardClone = cardTemplate.content.firstElementChild.cloneNode(true);
            cardClone = buildCardTemplate(cardClone, card);
            levelClone.appendChild(cardClone);
        });

        levelTemplate.appendChild(levelClone);
        board.appendChild(levelClone);
    });
}

function buildCardTemplate(cardClone, card) {
    cardClone.querySelector("img").src = `./assets/images/cards/${card.level}/${card.bonus}.webp`;
    cardClone.querySelector("header").textContent = `${card.prestigePoints} - ${card.bonus}`;
    cardClone.dataset.cardName = card.name;
    Object.entries(card.cost.tokens).forEach(([gem, count]) => { // Loop through and render cost of card
        const item = document.createElement("li");
        item.classList.add(gem.toLowerCase());
        item.classList.add("price-token");

        item.textContent = count;
        cardClone.querySelector("ul").appendChild(item);
    });

    return cardClone;
}

function renderNobles(gameInfo) {
    const noblePanel = document.querySelector("#nobles");
    const nobleTemplate = document.querySelector("#noble-template");
    noblePanel.innerHTML = '';

    gameInfo.unclaimedNobles.forEach(noble => {
        const nobleClone = nobleTemplate.content.firstElementChild.cloneNode(true);
        nobleClone.querySelector("header").textContent = `${noble.prestigePoints} - ${noble.name}`;
        nobleClone.querySelector("img").src = `./assets/images/nobles/${noble.name}.webp`;
        nobleClone.dataset.nobleName = noble.name;

        Object.entries(noble.neededBonuses.tokens).forEach(([gem, count]) => { // Loop through and render cost of card
            const item = document.createElement("li");
            item.classList.add(gem.toLowerCase());
            item.textContent = count;
            nobleClone.querySelector("ul").appendChild(item);

        });

        noblePanel.appendChild(nobleClone);
    });
}

function renderGems(gameInfo) {
    const gemPanel = document.querySelector("#gems-panel");
    gemPanel.innerHTML = '';

    Object.entries(gameInfo.unclaimedTokens.tokens).forEach(([gem, count]) => {
        const gemElement = document.createElement("div");
        gemElement.classList.add("gem", gem.toLowerCase());
        gemElement.innerHTML = `${gem} - <span class='gem-count'>${count}</span>`;

        gemPanel.appendChild(gemElement);
        // Check for tokens which you can choose twice
        if (count >= 4 && gem !== 'Gold') {
            TOKENS_TO_DOUBLE.push(gem.charAt(0).toUpperCase() + gem.slice(1).toLowerCase());
        }
    });

}

function renderInventory(gameInfo) {
    gameInfo.players.forEach(player => {
        if (Helper.playerIsCurrent(player.name)) {
            renderInventoryBuilt(player);
            renderInventoryTokens(player);
        }
    });
}

function renderInventoryTokens(player) {
    const $totalTokenCount = document.querySelector(".token-count-total span");
    const totalTokenCount = Object.values(player.tokens.tokens).reduce((acc, count) => acc + count, 0);
    $totalTokenCount.textContent = `${totalTokenCount}/10`;

    const $container = document.querySelector("#inventory-tokens");
    $container.innerHTML = '';
    if (player.tokens !== undefined) {
        Object.entries(player.tokens.tokens).forEach(([token, count]) => {
            $container.innerHTML += `
                <div class="token-count ${token.toLowerCase()}">${count}</div>
            `;
        });
    }
}

function renderInventoryBuilt(player) {
    const $container = document.querySelector("#inventory-built");
    $container.innerHTML = '';

    if (player.bought !== undefined) {
        player.bought.forEach(built => {
            const $template = document.querySelector("#built-template").content.firstElementChild.cloneNode(true);

            $template.querySelector("header").textContent = built.prestigePoints;
            $template.classList.add(built.bonus.toLowerCase());
            $container.insertAdjacentHTML("beforeend", $template.outerHTML);
        });
    }
}

function renderReservedCards(gameInfo) {
    const $reservedContainer = document.querySelector(".reserved-cards");
    $reservedContainer.innerHTML = '';

    const self = gameInfo.players.find(player => player.name === LocalStorageAbstractor.loadFromStorage("playerName"));
    if (!self) return;

    self.reserved.forEach(card => {
        const cardClone = document.querySelector("#reserved-card-template").content.firstElementChild.cloneNode(true);

        // Add data attributes for buying
        cardClone.dataset.cardName = card.name;
        // Set background color and text color dynamically
        cardClone.classList.add(card.bonus.toLowerCase());

        // Populate popup card
        const popup = cardClone.querySelector(".details-popup");
        popup.querySelector("header").textContent = `${card.prestigePoints} - ${card.bonus}`;
        popup.querySelector("img").src = `./assets/images/cards/${card.level}/${card.bonus}.webp`;
        popup.querySelector("img").alt = `${card.name}`;
        popup.dataset.cardName = card.name;

        // Populate cost tokens (grid layout with numbers)
        const costList = popup.querySelector(".cost-list");
        costList.innerHTML = '';
        Object.entries(card.cost.tokens).forEach(([gem, count]) => {
            const li = document.createElement("li");
            li.classList.add(gem.toLowerCase());
            li.textContent = count;
            costList.appendChild(li);
        });

        $reservedContainer.appendChild(cardClone);
    });
}




// Renders the full board, when performance issues we should build separate functions.
function renderFullBoard() {
    const gameId = LocalStorageAbstractor.loadFromStorage("gameId");
    return API.getGameInfo(gameId)
        .then((gameInfo) => {
            gameInfo = gameInfo.game;
            renderPlayers(gameInfo);
            renderNobles(gameInfo);
            renderInventory(gameInfo);
            renderDevelopmentCards(gameInfo);
            renderGems(gameInfo);
            renderReservedCards(gameInfo);
        })
        .catch((err) => {
            console.error("Error fetching game info:", err);
        });
}

export { renderInventory, renderGems, renderNobles, renderDevelopmentCards, renderPlayers, renderFullBoard, renderReservedCards, TOKENS_TO_DOUBLE };