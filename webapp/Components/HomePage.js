let homePage = `<h4 id="pageTitle">Home</h4>
<p>This frontend runs on Webpack and uses the Customizable Analog Clock npm package. 
Furthermore, the frontend has a proxy that allows to redirect 
the API requests.</p>
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