let id = Math.random();
const PrintError = (err) => {
    if (!err) return;
    let page = document.body;
    const errorDiv = document.createElement("div");
    errorDiv.className = "alert alert-danger mt-2";
    errorDiv.id = "errorBoard" + id;
    errorDiv.innerText = err.message;
    page.appendChild(errorDiv);
    setTimeout(() => document.getElementById("errorBoard" + id).remove(), 5000);
};

export default PrintError;
