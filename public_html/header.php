<nav class="navbar navbar-expand-md navbar-dark nav-background bg-mute">


      <div class="collapse navbar-collapse row" id="navbarsExample04">

              <!-- Logo -->
              <div class="header_logo col-lg-2">
                      <a class="navbar-brand header-logo" href="#"><img src="global_assets/img/logo.png" alt="Logo" height="40px"></img></a>
              </div>

              <!-- Text box for search -->
              <div class="search-pos col-lg-3 align_left">
                      <div class="float-left">
                              <form class="form-inline">
                                 <input class="form-control" type="text" placeholder="Search" list="search_list" size="60" id="search_term" onInput="JavaScript:search()" autoComplete="off"></input>
                                        <datalist id="search_list">
                                                <!-- option value="HTML">
                                                <option value="CSS"  -->
                                        </datalist>
                              </form>
                      </div>
              </div>

                <!-- Page links -->
                <div class="col-lg-7">
                        <ul class="z-back navbar-nav float-right">
                          <li class="nav-item active">
                            <a class="nav-link" href="index.php">Dashboard <span class="sr-only"></span></a>
                          </li>
                          <li class="nav-item">
                            <a class="nav-link" href="portfolio.php">Porfolio</a>
                          </li>

                          <!-- Login link or username -->
                          <li class="nav-item">
                            <p> <span id='display_name'><a class="nav-link" href="login.php">Log In</a></span> </p>
                          </li>

                        </ul>
                </div>
      </div>
   </nav>
