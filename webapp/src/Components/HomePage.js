let homePage = `<h4 id="pageTitle">Home</h4>
<p>Homepage</p>
`;

const HomePage = async () => {  
  let page = document.querySelector("#page");
  page.innerHTML  = homePage + `<div class="d-flex justify-content-center">
                      <div 
                          id="my-clock"                          
                          style="width: 200px; height: 200px;"                         
                      </div>
                        </div>`; 
;
 
};

export default HomePage;