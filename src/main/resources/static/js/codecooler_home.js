function showQuests() {
    document.getElementById("quests").style.display = "block";
    document.getElementById("artifacts").style.display = "none";
    document.getElementById("quests_button").classList.remove("redbutton");
    document.getElementById("quests_button").classList.add("bluebutton");
    document.getElementById("artifacts_button").classList.add("redbutton");
    document.getElementById("artifacts_button").classList.remove("bluebutton");
}

function showArtifacts() {
    document.getElementById("artifacts").style.display = "block";
    document.getElementById("quests").style.display = "none";
    document.getElementById("artifacts_button").classList.remove("redbutton");
    document.getElementById("artifacts_button").classList.add("bluebutton");
    document.getElementById("quests_button").classList.add("redbutton");
    document.getElementById("quests_button").classList.remove("bluebutton");
}