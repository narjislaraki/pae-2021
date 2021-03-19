import { getUserSessionData} from "../utils/session.js";
import { RedirectUrl } from "./Router.js";
import Navbar from "./Navbar.js";
import callAPI from "../utils/api.js";
import PrintError from "./PrintError.js";

let smallImg1 = document.getElementById("small-img1");
let smallImg2 = document.getElementById("small-img2");
let smallImg3 = document.getElementById("small-img3");
let smallImg4 = document.getElementById("small-img4");
let smallImg5 = document.getElementById("small-img5");

let furniturePage = 
`

`;

const FurniturePage = () => {  
    let page = document.querySelector("#page");
    page.innerHTML = furniturePage;
    
    const user = getUserSessionData();
  };



function gallerySlides(smallImg){
    let bigImg = document.getElementById("big-img");
    bigImg.src = smallImg.src;
}

smallImg1.addEventListener("mouseover", () => { gallerySlides(smallImg1); });
smallImg2.addEventListener("mouseover", () => { gallerySlides(smallImg2); });
smallImg3.addEventListener("mouseover", () => { gallerySlides(smallImg3); });
smallImg4.addEventListener("mouseover", () => { gallerySlides(smallImg4); });
smallImg5.addEventListener("mouseover", () => { gallerySlides(smallImg5); });

  export default FurniturePage;