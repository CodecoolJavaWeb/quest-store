function enableEdit(element) {
    element.nextElementSibling.style.display = "initial";
    element.style.display = "none";
    element.parentElement.previousElementSibling.getElementsByTagName("input")[0].disabled = false;
    var inputs = element.parentElement.getElementsByTagName("input");
    for(var i = 0; i < inputs.length; i++){
        inputs[i].disabled = false;
    }
}

function enableAdd() {
    document.getElementById("addLevel").style.display = "block";
}