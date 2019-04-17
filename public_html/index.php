<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="global_assets/dashboard.css">
        <link rel="stylesheet" type="text/css" href="global_assets/header.css">

        <title>Stock Overflow</title>
        <link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">


</head>
<body>

        <div class="header_div navbar-spacers">
            <?php include "header.php"; ?>
            <!-- Use jquery to insert header content here if PHP fails    -->
        </div>

<!-- Body of index -->
<div class="">
                <div class="row">
                        <div class="offset-lg-2 col-lg-5 bar-right">
                                <h3>Summary</h3>
                                <img src="global_assets/img/graph.png" class="img-fluid">
                        </div>
                        <div class="col-lg-3">
                                <!-- List of Stocks -->
                                <table class = "table">
                                        <h4> Stocks </h4>
                                   <thead>
                                      <tr>
                                         <th>Stock</th>
                                         <th>Shares</th>
                                         <th>Graph</th>
                                         <th>Price</th>
                                      </tr>
                                   </thead>

                                   <tbody>
                                      <tr>
                                         <td>AMD</td>
                                         <td>2</td>
                                         <td>Graph</td>
                                         <td>Price</td>
                                      </tr>
                                   </tbody>
                                </table>

                                <!-- Watchlist -->
                                <table class = "table">
                                         <h4> Watch List </h4>
                                   <thead>
                                      <tr>
                                         <th>Stock</th>
                                         <th>Graph</th>
                                         <th>Price</th>
                                      </tr>
                                   </thead>

                                   <tbody>
                                      <tr>
                                         <td>SNE</td>
                                         <td>Graph</td>
                                         <td>Price</td>
                                      </tr>
                                   </tbody>

                                </table>
                        </div>
                        <div class="col-lg-2">
                        </div>
                </div>
                <div class="offset-lg-2">
                        <h3>Top Movers</h3>
                        <p> Articles go here </p>
                </div>
                <div class="offset-lg-2">
                        <h3>Relevant articles</h3>
                        <div class="row">
                                <div class="col-lg-2 col-md-2 col-sm-2">
                                        <a href="#" class="bg-mute round arrow">Previous &laquo;</a>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6">

                                        <!-- First Article -->
                                        <div class="card mb-3" style="max-width: 100%;">
                                          <div class="row no-gutters">
                                            <div class="col-md-4">
                                              <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="card-img" alt="Image Unavailable">
                                            </div>
                                            <div class="col-md-8">
                                              <div class="card-body">
                                                <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                              </div>
                                            </div>
                                          </div>
                                        </div>

                                        <!-- Second Article -->
                                        <div class="card mb-3" style="max-width: 100%;">
                                          <div class="row no-gutters">
                                            <div class="col-md-4">
                                              <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="card-img" alt="Image Unavailable">
                                            </div>
                                            <div class="col-md-8">
                                              <div class="card-body">
                                                <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                              </div>
                                            </div>
                                          </div>
                                        </div>

                                        <!-- Third Article -->
                                        <div class="card mb-3" style="max-width: 100%;">
                                          <div class="row no-gutters">
                                            <div class="col-md-4">
                                              <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="card-img" alt="Image Unavailable">
                                            </div>
                                            <div class="col-md-8">
                                              <div class="card-body">
                                                <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                              </div>
                                            </div>
                                          </div>
                                        </div>

                                        <!-- Fourth Article -->
                                        <div class="card mb-3" style="max-width: 100%;">
                                          <div class="row no-gutters">
                                            <div class="col-md-4">
                                              <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="card-img" alt="Image Unavailable">
                                            </div>
                                            <div class="col-md-8">
                                              <div class="card-body">
                                                <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                              </div>
                                            </div>
                                          </div>
                                        </div>

                                </div>
                                <div class="col-lg-2 col-md-2 col-sm-2">
                                        <a href="#" class="bg-mute round float-right arrow"> Next &raquo;</a>
                                </div>
                        </div>

                </div>

</div>
<div class="footer_div">
    <?php include "footer.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>

</body>
</html>
