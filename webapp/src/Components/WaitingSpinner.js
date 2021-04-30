const waitingSpinner = (element = null) => {
    if (!element)
        document.getElementById("page").innerHTML = `<div id="ringdiv"><div class="lds-ring"><div></div><div></div><div></div><div></div></div></div>`
    else
        element.innerHTML = `<div class="lds-roller"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>`
}

export default waitingSpinner;