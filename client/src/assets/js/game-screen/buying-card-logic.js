import * as API from "../api.js";
import * as Helper from "../util/helpers.js";
import { showModal } from "../util/modal.js";
import {renderFullBoard} from './renderer.js';
import {gameLoop} from "../game-screen.js";
import * as LocalStorageAbstractor from '../data-connector/local-storage-abstractor.js';

function toggleDevelopmentEventListeners(bool) {
    const cards = document.querySelectorAll("#developments-panel .card");
    cards.forEach((card) => {
        if (bool) {
            card.addEventListener("click", handleCardClick);
        } else {
            card.removeEventListener("click", handleCardClick);
        }
    });

}

function handleCardClick(e) {
    const cardName = e.currentTarget.dataset.cardName ;

    showModal({
        title: "Choose Action",
        body: `What do you want to do with ${cardName}?`,
        buttons: [
            { text: "Buy", value: "buy" },
            { text: "Reserve", value: "reserve" },
            { text: "Cancel", value: "cancel" }
        ],
        onClose: (choice) => handleCardAction(choice, cardName)
    });
}

function handleCardAction(choice, cardName) {
    if (choice === "buy") {
        Helper.hasGold().then((hasGold) => {
            handlePurchase(cardName, hasGold);
        });
    } else if (choice === "reserve") {
        reserveCard(cardName).catch(err => {
            console.error(err);
        });
    } else {
       return; // Cancel action, do nothing
    }
}

function handlePurchase(cardName, hasGold) {
    if (hasGold) {
        showGoldUsageModal(cardName);
    } else {
        buyCard(cardName).catch(err => {
            console.error(err);
        });
    }
}

function showGoldUsageModal(cardName) {
    buildFormTemplate(cardName).then(($formTemplate) => {
        showModal({
            title: "Do you want to use any gold?",
            body: $formTemplate,
            buttons: [
                { text: "Buy", value: "buy" },
                { text: "Cancel", value: "cancel" }
            ],
            onClose: (choice) => {
                if (choice === "buy") {
                    handleGoldBuy(cardName);
                }
            }
        });
    });
}

function handleGoldBuy(cardName) {
    const modalElement = document.querySelector('#modal-overlay');
    const goldInputs = modalElement.querySelectorAll(".gold-counters");
    const goldAmounts = {};
    goldInputs.forEach(input => {
        const token = input.name.toUpperCase();
        goldAmounts[token] = parseInt(input.value);
    });

    buyCard(cardName, goldAmounts).catch(err => {
        console.error(err);
    });
}

function buyCard (cardName, goldUsed = {}){
    return Helper.getDevelopmentDeductedPrice(cardName).then((deductedCost) => {
        const adjustedCost = Helper.getAdjustedCostWithGoldUsage(deductedCost, goldUsed);
        return API.buyDevelopment(cardName, adjustedCost);
    })
    .then(() => {
        showModal({
            title: "Success",
            body: "Successfully bought development. Continuing to next player.",
            buttons: [{ text: "OK", value: "ok" }]
        });
    })
    .then(() => renderFullBoard())
    .then(() => gameLoop())
    .catch((err) => {
        showModal({
            title: "Cannot Buy",
            body: "You don't have enough tokens to buy this card.",
            buttons: [{ text: "OK", value: "ok" }]
        });
        console.error(err);
    });
}

function reserveCard(cardName){
    return API.reserveDevelopment(cardName)
        .then(() => renderFullBoard())
        .then(() => gameLoop())
        .catch((err) => {
            showModal({
                title: "Reserve Failed",
                body: err.cause,
                buttons: [{ text: "OK", value: "ok" }]
            });
            console.error(err);
        });
}

function buildFormTemplate(cardName) {
    const $template = document.querySelector("#modal-template-choose-form").content.cloneNode(true);
    const $leftColumn = $template.querySelector("#left-column ul");
    const $rightColumn = $template.querySelector("#right-column ul");

    return Helper.developmentPriceByName(cardName)
        .then((price) => {
            fillPriceColumns($leftColumn, $rightColumn, price);
        })
        .then(() => {
            const gameId = LocalStorageAbstractor.loadFromStorage("gameId");
            return API.getGameInfo(gameId);
        })
        .then((gameInfo) => {
            updateGoldInfo($template, gameInfo);
            return $template;
        });
}

function fillPriceColumns($leftColumn, $rightColumn, price) {
    Object.entries(price).forEach(([token, amount]) => {
        const tokenLower = token.toLowerCase();
        $leftColumn.insertAdjacentHTML("beforeend", `
            <li>
                <div class="gem ${tokenLower}">
                    <span class="gem-type">${token}</span>: <span class="amount">${amount}</span>
                </div>
            </li>
        `);
        $rightColumn.insertAdjacentHTML("beforeend", `
            <li>
                <label>${token}:
                    <input type="number" class="gold-counters" name="${tokenLower}" min="0" value="0" />
                </label>
            </li>
        `);
    });
}

function updateGoldInfo($template, gameInfo) {
    const gold = gameInfo.game.currentPlayer.tokens.tokens.GOLD;
    $template.querySelector(".gold-count").textContent = gold;
    $template.querySelectorAll(".gold-counters").forEach(($counter) => {
        $counter.setAttribute("max", gold);
    });
}

export {toggleDevelopmentEventListeners, buildFormTemplate};