import * as API from "../api.js";
import { TOKENS_TO_DOUBLE, renderFullBoard } from './renderer.js';
import { toggleReserveEventListeners } from "./reserving-cards-logic.js";
import { toggleDevelopmentEventListeners } from './buying-card-logic.js';
import { toggleNobleEventListeners } from "./taking-noble-logic.js";
import { gameLoop } from '../game-screen.js';
import {showModal} from "../util/modal.js";

let TOKENS_TURN = {};

function toggleGemEventListeners(handler, active) {
    const $gems = document.querySelectorAll("#gems-panel .gem");
    $gems.forEach((gem) => {
        if (gem.classList[1] !== "gold") {
            if (active) {
                gem.addEventListener("click", handler);
            } else {
                gem.removeEventListener("click", handler);
            }
        }
    });
}

function tokenFirstStep(e) {
    toggleDevelopmentEventListeners(false);
    toggleNobleEventListeners(false);

    const token = getTokenName(e);
    TOKENS_TURN[token] = 1;
    tokenStepDecrement(e);

    toggleGemEventListeners(tokenFirstStep, false);
    toggleGemEventListeners(tokenSecondStep, true);
    toggleReserveEventListeners(false);

    showModal({
        title: "Panos Boss",
        body: "You can take a second token, either the same or a different one.",
        buttons: [{ text: "OK", value: "ok" }]
    });
}
function handleDifferentToken(e, token) {
    TOKENS_TURN[token] = 1;
    tokenStepDecrement(e);
    toggleGemEventListeners(tokenSecondStep, false);
    toggleGemEventListeners(tokenThirdStep, true);
    showModal({
        title: "Panos Boss",
        body: "You can take a third token, but it must be a different one.",
        buttons: [{ text: "OK", value: "ok" }]
    });
}

function handleDoubleToken(e, token) {
    TOKENS_TURN[token] = 2;
    tokenStepDecrement(e);
    toggleGemEventListeners(tokenSecondStep, false);
    finalTakeTokens();
}

function tokenSecondStep(e) {
    const token = getTokenName(e);

    if (!(token in TOKENS_TURN)) {
        handleDifferentToken(e, token);
    } else if (TOKENS_TO_DOUBLE.includes(token)) {
        handleDoubleToken(e, token);
    } else {
        showModal({
            title: "Panos Boss",
            body: "You can not take this token.",
            buttons: [{ text: "OK", value: "ok" }]
        });
    }
}

function tokenThirdStep(e) {
    const token = getTokenName(e);
    if (!(token in TOKENS_TURN)) {
        TOKENS_TURN[token] = 1;
        tokenStepDecrement(e);
        toggleGemEventListeners(tokenThirdStep, false);
        finalTakeTokens();
    } else {
        showModal({
            title: "Panos Boss",
            body: "You can not take this token.",
            buttons: [{ text: "OK", value: "ok" }]
        });
    }
}

function getTokenName(e) {
    const token = e.currentTarget.classList[1]; // Non-capital (ruby)
    return token.charAt(0).toUpperCase() + token.slice(1); // Capital (Ruby)
}

function tokenStepDecrement(e) {
    const $gemCount = e.currentTarget.querySelector(".gem-count");
    $gemCount.textContent = (parseInt($gemCount.textContent) - 1).toString();
}
function resetTokenSelection() {
    TOKENS_TURN = {};
    renderFullBoard().then(() => {
        toggleGemEventListeners(tokenFirstStep, true);
        toggleGemEventListeners(tokenSecondStep, false);
        toggleGemEventListeners(tokenThirdStep, false);
        toggleDevelopmentEventListeners(true);
        toggleNobleEventListeners(true);
        toggleReserveEventListeners(true);
        showModal({
            title: "Panos Boss",
            body: "You can select tokens again.",
            buttons: [{ text: "OK", value: "ok" }]
        });
    });
}

function finalTakeTokens() {
    const tokensList = Object.entries(TOKENS_TURN).map(([token, count]) => `${token}: ${count}`).join(', ');
    
        showModal({
        title: "Confirm Token Selection",
        body: `You have selected the following tokens: ${tokensList}. Do you want to proceed?`,
        buttons: [
            { text: "Confirm", value: "confirm" },
            { text: "Cancel", value: "cancel" }
        ],
        onClose: (response) => {
            if (response === "confirm") {
                API.takeTokens(TOKENS_TURN)
                .then(() => {
                    TOKENS_TURN = {};
                    return renderFullBoard();
                })
                .then(() => gameLoop())
                .catch((err) => console.error("Error taking tokens:", err));
            } else {
                resetTokenSelection();
            }
        }
    });
}

export { toggleGemEventListeners, tokenFirstStep, tokenSecondStep, tokenThirdStep, getTokenName, tokenStepDecrement, finalTakeTokens };