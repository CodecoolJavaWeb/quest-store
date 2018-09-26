function enableEdit(element) {
    element.nextElementSibling.style.display = "initial";
    element.style.display = "none";
    element.parentElement.getElementsByClassName("stdinput")[0].disabled = false;
}

function save(element) {
    element.previousElementSibling.style.display = "initial";
    element.style.display = "none";
    element.parentElement.getElementsByClassName("stdinput")[0].disabled = true;
}