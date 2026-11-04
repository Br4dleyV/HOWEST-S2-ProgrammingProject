function showModal({ title, body = "", buttons = [], onClose }) {
    const template = document.querySelector("#modal-template").content.cloneNode(true);
    const overlay = template.querySelector("#modal-overlay");
    const bodyElem = template.querySelector("#modal-body");
    const buttonsElem = template.querySelector("#modal-buttons");

    template.querySelector("#modal-title").textContent = title;
    setBodyContent(body, bodyElem);
    buttonsElem.innerHTML = "";

    buttons.forEach(({ text, value }) => {
        const btn = document.createElement("button");
        btn.textContent = text;
        btn.addEventListener("click", () => {
            if (typeof onClose === "function") onClose(value);
            document.body.removeChild(overlay);
        });
        buttonsElem.appendChild(btn);
    });

    overlay.style.display = "flex";
    document.body.appendChild(overlay);
}

function setBodyContent(content, bodyElem) {
    if (typeof content === "string") {
        bodyElem.innerHTML = content;
    } else if (content instanceof Node) {
        bodyElem.innerHTML = "";
        bodyElem.appendChild(content);
    } else {
        console.error("Content must be a string or a Node element.");
        return;
    }
}

export { showModal };