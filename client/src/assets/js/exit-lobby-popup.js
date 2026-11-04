function setupExitLobbyPopup() {
    // Select buttons and popup
    const popup = document.querySelector('#exit-lobby');
    const closeScreen = document.querySelector('#close-screen');

    // Check URL param
    const params = new URLSearchParams(window.location.search);
    if (params.get('popup') === 'true') {
        popup.classList.add('active');
        popup.classList.remove('hidden');
        document.body.classList.add('popup-open');
    }

    // Hide popup on back
    if (!closeScreen) return; // Check if closeScreen exists before adding event listener
    closeScreen.addEventListener('click', () => {
        popup.classList.remove('active');
        popup.classList.add('hidden');
        document.body.classList.remove('popup-open');
        window.history.replaceState(null, "");
    });
};

export { setupExitLobbyPopup };