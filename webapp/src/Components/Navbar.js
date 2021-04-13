let navBar = document.querySelector(".navigationbar");
import callAPI from "../utils/api.js";
import { removeSessionData, currentUser, resetCurrentUser, getUserSessionData } from "../utils/session.js";
import PrintError from "./PrintError.js";
import PrintMessage from "./PrintMessage.js";
import { RedirectUrl } from "./Router.js";
let userData = getUserSessionData;

// destructuring assignment
const Navbar = async () => {
  let nb;
  let user = currentUser;

  if (user) {
    nb = `
    <h1 class="lines" ></h1>
        <div class= "title">
          <a id="home" href="#">
            <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
            <img class="logo-writing" src="assets/lvs.svg" alt="logo">
          </a>
        </div>
        <button id="all-furnitures-navbar" class="condensed small-caps">
          Voir tous les meubles
        </button>
        <div id="category" class="dropdown">
          <span class=" dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
            Types de meuble
          </span>
          <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
            <li><a class="dropdown-toggle-item" href="#">Type1</a></li>
            <li><a class="dropdown-toggle-item" href="#">Type2</a></li>
            <li><a class="dropdown-toggle-item" href="#">Type3</a></li>
          </ul>
        </div>
        <button id="btnIntroduceVisit" class="btn btn-dark btn-navbar condensed small-caps">
          Demander une visite
        </button>

        <div class="user-menu">
        
          <p class="text-user" id="bonjour">Bonjour,</p>
          
          <p id="username-menu" class="text-user">${user.username}</p>
            <div class="dropleft" id="head-menu">
              <i id="user" class="bi bi-person-circle dropdown-toggle-user"></i>
              <ul class="dropdown-menu dropdown-menu-left condensed" aria-labelledby="dropdownMenuButton1">
                <li><a class="dropdown-item" id="profile" href="#">Profile</a></li>
                <li><a class="dropdown-item" id="logout" href="#">Se déconnecter</a></li>
              </ul>
            </div>
            <div id="adminToolsIcon">          
            </div>
        </div>

        <div class="hover_bkgr_fricc"">
        <span class="helper"></span>
        <div>
            <div class="popupCloseButton"">&times;</div>
            <h2>Introduire une demande de visite</h2>
          <form class="RequestVisitForm">
          <div>
            <h4>Plage horaire : </h4><input type="text" id="timeSlot" placeholder="exemple : le lundi de 18h à 22h" required><br>
            <h4>Adresse : (uniquement si elle est différente de votre addresse)</h4>
          </div>
          <div>
            <input type="text" class="form-control input-card" id="street" placeholder="Rue" required>

            <input type="text" class="form-control input-card" id="number" placeholder="Numéro" required>
                  
            <input type="text" class="form-control input-card" id="unitnumber" placeholder="Boite">
                  
            <input type="text" class="form-control input-card" id="postcode" placeholder="Code Postal" required>
                  
            <input type="text" class="form-control input-card" id="city" placeholder="Commune" required>
                  
            <input type="text" class="form-control input-card" id="country" placeholder="Pays" required>
              
          </div><br>
          <h4>Meuble(s) : </h4><br>
          <div id="typeFurniture" class="dropdown">
            <label for="type">Type du meuble : </label>
            <div id="tousLesTypes"></div>
          </div>
          <br>
          <button class="btn btn-outline-success" id="introduceBtn" name="introduceBtn" type="submit">Confirmer</button>
          <button class="btn btn-outline-danger" id="cancelBtn" name="cancelBtn" type="submit">Annuler</button>
          </form>

          <span class="btnClose"></span>
        </div>
    </div>
        `;
    navBar.innerHTML = nb;

    let introduceBtn = document.getElementById("introduceBtn");
    introduceBtn.addEventListener("click", onIntroduceRequest);
    let cancelBtn = document.getElementById("cancelBtn");
    cancelBtn.addEventListener("click", onCloseVisit);

    Array.from(document.getElementsByName("trigger_popup_fricc")).forEach((e) => {
      e.addEventListener("click", onIntroduceVisit);
    });
    Array.from(document.getElementsByClassName("popupCloseButton")).forEach((e) => {
      e.addEventListener("click", onCloseVisit);
    })

    try {
      const typesOfFurniture = await callAPI(
        "/api/furnitures/typeOfFurnitureList",
        "GET",
        undefined,
        undefined);
      onTypesOfFurniture(typesOfFurniture);
    } catch (err) {
      console.error("Navbar::onIntroduceVisit", err);
      PrintError(err);
    }

    if (user.role == "ADMIN") {
      let adminTools = document.getElementById("adminToolsIcon");
      adminTools.innerHTML = `<img src="../assets/key4Admin.png" alt="key" id="keyAdmin" width="30" height="30">`;
      //adminTools.innerHTML = `<i class="bi bi-key-fill" id="keyAdmin" width="30" height="30"></i>`;
      let keyAdmin = document.getElementById("keyAdmin");
      keyAdmin.addEventListener("click", onClickTools);
    }
    else {
      let userMenu = document.querySelector(".user-menu");
      userMenu.style.gridTemplateColumns = "25% 25% 25% 25%";
    }
  } else {
    nb = `<h1 class="lines" ></h1>
    <div class= "title">
      <a id="home" href="#">
        <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
        <img class="logo-writing" src="assets/lvs.svg" alt="logo">
      </a>
    </div>
    <button id="all-furnitures-navbar" class="condensed small-caps">
        Voir tous les meubles
    </button>
    <div id="category" class="dropdown">
      <span class=" dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
        Types de meuble
      </span>
      <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
        <li><a class="dropdown-toggle-item" href="#">Type1</a></li>
        <li><a class="dropdown-toggle-item" href="#">Type2</a></li>
        <li><a class="dropdown-toggle-item" href="#">Type3</a></li>
      </ul>
    </div>

    <a class="btn btn-dark btn-navbar condensed small-caps" href="#" data-uri="/login">S'identifier</a>`;
    navBar.innerHTML = nb;
  }

  if (user) {
    let logout = document.querySelector("#logout");
    logout.addEventListener("click", onLogout);

    let btnIntroduceVisit = document.getElementById("btnIntroduceVisit");
    btnIntroduceVisit.addEventListener("click", onIntroduceVisit);
  }
  let voir = document.getElementById("all-furnitures-navbar");
  voir.addEventListener("click", onFurnitureListPage);

  let home = document.getElementById("home");
  home.addEventListener("click", onHomePage);

};

const onFurnitureListPage = (e) => {
  e.preventDefault();
  RedirectUrl("/furnitures");
};
const onHomePage = (e) => {
  e.preventDefault();
  RedirectUrl("/");
};

const onLogout = (e) => {
  e.preventDefault();
  removeSessionData();
  resetCurrentUser();
  RedirectUrl("/");
  Navbar();
};

const onClickTools = (e) => {
  e.preventDefault();
  RedirectUrl("/visits")
}

const onIntroduceVisit = (e) => {
  e.preventDefault();
  Array.from(document.getElementsByClassName("hover_bkgr_fricc")).forEach((element) => {
    element.style.display = "block";
  });
}

const onCloseVisit = (e) => {
  e.preventDefault();
  Array.from(document.getElementsByClassName("hover_bkgr_fricc")).forEach((element) => {
    element.style.display = "none";
  });
}


const onTypesOfFurniture = (data) => {
  let eachType = '<select name="type" id="type">';
  eachType += data
    .map((type) =>
      `<option value="${type.id}">${type.label}</option>`
    ).join("");
  let tousLesTypes = document.getElementById("tousLesTypes");
  tousLesTypes.innerHTML = eachType;
  tousLesTypes.innerHTML += `</select>`
}




const onIntroduceRequest = async (e) => {
  e.preventDefault();
  let request = {
    timeSlot: document.getElementById("timeSlot").value,
    warehouseAddress: {
      street: document.getElementById("street").value,
      buildingNumber: document.getElementById("number").value,
      postCode: document.getElementById("postcode").value,
      country: document.getElementById("country").value,
      city: document.getElementById("city").value
    }
  };
  if (document.getElementById("unitnumber").value != "") {
    request.warehouseAddress.unitNumber = document.getElementById("unitNumber").value;
  }
  try {
    const requestVisit = await callAPI(
      "/api/visits/introduce",
      "POST",
      userData.token,
      request
    );
    if (requestVisit) {
      RedirectUrl("/");
      PrintMessage("Votre demande de visite a bien été enregistrée. Elle est maintenant en attente de confirmation.");
    }

  } catch (err) {
    console.error("Navbar :: onIntroduceRequest", err);
    PrintError(err);
  }
}


export default Navbar;
