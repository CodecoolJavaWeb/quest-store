function enableEdit(element) {
    element.nextElementSibling.style.display = "initial";
    element.style.display = "none";
    element.parentElement.getElementsByClassName("stdinput")[0].disabled = false;
}

function save() {
    var inputs = document.body.getElementsByClassName("stdinput")
    for(var i = 0; i < inputs.length; i++){
        inputs[i].disabled = false;
    }
}