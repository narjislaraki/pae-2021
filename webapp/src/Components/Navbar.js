let navBar = document.querySelector(".navbar");
import {getUserSessionData} from "../utils/session.js";
// destructuring assignment
const Navbar = () => {
  let nb;
  let user = getUserSessionData();    
  if (user) {
    nb = `
    <h1 class="lines" ></h1>
        <div class= "title">
          <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
          <img class="logo-writing" src="assets/lvs.svg" alt="logo">
        </div>
        <div id="category" class="dropdown">
          <span class="btn btn-secondary dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
            Types de meuble
          </span>
          <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
            <li><a class="dropdown-item" href="#">Type1</a></li>
            <li><a class="dropdown-item" href="#">Type2</a></li>
            <li><a class="dropdown-item" href="#">Type3</a></li>
          </ul>
        </div>

        <div class="user-head">
          <p class="text-user">Bonjour,</p>
          <p id="username" class="text-user">${user.user.login}</p>
          <i id="user" class="bi bi-person-circle"></i>
        </div>
        `;
  } else {
    nb = `<h1 class="lines" ></h1>
    <div class= "title">
      <img class="rect-logo" src="assets/rectangle.svg" alt="rectangle logo">
      <img class="logo-writing" src="assets/lvs.svg" alt="logo">
    </div>
    <div id="category" class="dropdown">
      <span class="btn btn-secondary dropdown-toggle condensed small-caps" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
        Types de meuble
      </span>
      <ul class="dropdown-menu condensed" aria-labelledby="dropdownMenuButton1">
        <li><a class="dropdown-item" href="#">Type1</a></li>
        <li><a class="dropdown-item" href="#">Type2</a></li>
        <li><a class="dropdown-item" href="#">Type3</a></li>
      </ul>
    </div>

    <a class="btn btn-dark btn-navbar condensed small-caps" href="#" data-uri="/login">S'identifier</a>`;
  }

  navBar.innerHTML = nb;

};

export default Navbar;
