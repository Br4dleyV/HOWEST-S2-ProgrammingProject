const GROUPNUMBER = "20";
const GROUPTOKEN = "Group20-3255-463";

const LOCALSERVER = `http://localhost:8080`;
const DEPLOYEDSERVER = `https://project-1.ti.howest.be/2024-2025/splendor/api`;
const GROUPDEPLOYEDSERVER = `https://project-1.ti.howest.be/2024-2025/group-${GROUPNUMBER}/api`;

// Game screen constants
const MODAL_DISPLAY_TIME = 5000; // Time in milliseconds to display the modal
const SCORE_THRESHOLD_MIN = 10; // Minimum score to trigger the modal
const SCORE_THRESHOLD_MAX = 14; // Maximum score to trigger the modal

function getAPIUrl() {
  return GROUPDEPLOYEDSERVER;
}

export { getAPIUrl, GROUPTOKEN, MODAL_DISPLAY_TIME, SCORE_THRESHOLD_MIN, SCORE_THRESHOLD_MAX };