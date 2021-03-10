import {Router} from "./Components/Router.js";
import Navbar from "./Components/Navbar.js";
/* use webpack style & css loader*/
import "./stylesheets/style.css";
/* load bootstrap css (web pack asset management) */
import 'bootstrap/dist/css/bootstrap.css';
/* load bootstrap module (JS) */
import 'bootstrap';



const FOOTER_CONTENT = `
<p id="copyright" class="condensed">© 2021 Li Vi Satcho</p>
  <p id="rgpd" class="condensed small-caps">Ce site respecte les normes RGPD</p>
  <div id="contact" class="condensed">
    <h4 id="lvs-footer">LI VI SATCHO</h4>
    <p class="adress-footer">Sentier des Artistes, 1bis</p>
    <p class="adress-footer">4800 Verviers</p>
    <p class="adress-footer">E-mail: contact@livisatcho.be</p>
  </div>
  <img id="devanture" src="assets/logoAE_v2.png" alt="devanture">
`;

Navbar();

Router();

// deal with header and footer
document.querySelector("#footerContent").innerHTML = FOOTER_CONTENT;
