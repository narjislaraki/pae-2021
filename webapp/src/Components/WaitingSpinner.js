const waitingSpinner = () => {
    document.getElementById("page").innerHTML = `<div id="ringdiv"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>`
}

export default waitingSpinner;