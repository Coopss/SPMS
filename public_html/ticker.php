<!DOCTYPE html>
<html>
<head>
        <link rel="stylesheet" type="text/css" href="global_assets/dashboard.css">
        <link rel="stylesheet" type="text/css" href="global_assets/header.css">
        <link rel="stylesheet" type="text/css" href="global_assets/ticker.css">

        <title>Stock Overflow</title>
        <link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">


</head>
<body>

    <!-- header -->
    <div class="header_div navbar-spacers">
        <?php include "header.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
    </div>

    <!-- Body of index -->
    <div class="">
                    <div class="row">
                            <div class="offset-lg-2 col-lg-5 bar-right">
                                    <h3>Stock Name Here</h3>
                                    <img src="global_assets/img/graph.png" class="img-fluid">
                            </div>
                            <div class="col-lg-3">
                                    <a href="#">Add to Watchlist</a><p></p>
                                    <table class = "table">


                                       <tbody>
                                          <th colspan="2">
                                             <b>Add to porfolio</b>
                                          </th>
                                          <tr>
                                             <td>Shares</th>
                                             <td> SHARE COUNT</th>
                                          </tr>
                                          <tr>
                                             <td> Date Purchased </td>
                                             <td> Date </td>
                                          </tr>
                                          <tr>
                                             <td> Time Purchased </td>
                                             <td> TIME </td>
                                          </tr>
                                          <tr>
                                             <td colspan="2" align="center">
                                                     <a href="#">Add</a>
                                             </td>
                                       </tbody>
                                    </table>

                                    <!-- Watchlist -->
                                    <table class = "table">

                                       <tbody>
                                          <tr>
                                             <th colspan="2">
                                                     <b>Statistics</b>
                                             </th>
                                          </tr>
                                          <tr>
                                             <td>Previous Close</td>
                                             <td> Value</td>
                                          </tr>
                                          <tr>
                                             <td>Open</td>
                                             <td>Value</td>
                                          </tr>
                                          <tr>
                                             <td>High</td>
                                             <td>Value</td>
                                          </tr>
                                          <tr>
                                             <td>Low</td>
                                             <td>Value</td>
                                          </tr>
                                          <tr>
                                             <td>52 Wk. High</td>
                                             <td>Value</td>
                                          </tr>
                                          <tr>
                                             <td>52 Wk. Low</td>
                                             <td>Value</td>
                                          </tr>
                                          <tr>
                                             <td>Volume</td>
                                             <td>Value</td>
                                          </tr>
                                          <tr>
                                             <td>Avg. Volume</td>
                                             <td>Value</td>
                                          </tr>
                                       </tbody>

                                    </table>
                            </div>
                            <div class="col-lg-2">
                            </div>
                    </div>
                    <div class="offset-lg-2">
                            <h3>About</h3>
                            <p>More info about a stock goes here if available</p>
                    </div>
                    <div class="offset-lg-2">
                            <h3>Relevant articles</h3>
                            <div class="row">
                                    <div class="col-lg-2">
                                            <a href="#" class="bg-mute round arrow">Previous &laquo;</a>
                                    </div>
                                    <div class="col-lg-6">
                                            <h3> Articles </h3>
                                    </div>
                                    <div class="col-lg-2">
                                            <a href="#" class="bg-mute round float-right arrow"> Next &raquo;</a>
                                    </div>
                            </div>

                    </div>

    </div>

    <!-- Insertion of footer -->
    <div class="footer_div">
        <?php include "footer.php"; ?>
            <!-- Use jquery to insert header content here if PHP fails    -->
    </div>

</body>
