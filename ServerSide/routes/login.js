var express = require('express');
var router = express.Router();
var bcrypt = require('bcrypt-nodejs');

/* GET home page. */
router.post('/', function(req, res) {
  
  var username = req.body.username;
  var password = req.body.password;

  var message;

  // Set our internal DB variable
  var db = req.db;

  // Set our collection
  var collection = db.get('users');

  collection.find( { "username" : username }, {}, function (err, doc) {
  	if (err || doc.length == 0) {
  		message = "Incorrect username/password!";
  		res.send(message);
  	}

  	else {
  		bcrypt.compare(password, doc[0].password, function(err, resp) {
  			
  			if (resp == true) {
  				message = "Success!"
  				res.send(message);
  			}

  			else {
  				message = "Incorrect username/password!"
  				res.send(message);
  			} 		
  		
  		});
  	}
  
  });
});


  /* GET home page. */
router.post('/authenticate', function(req, res) {
  
  var username = req.body.username;
  var password = req.body.password;

  // Set our internal DB variable
  var db = req.db;

  // Set our collection
  var collection = db.get('users');

  if (username == "admin") {

  	collection.find( { "username" : username }, {}, function (err, doc) {
  	
  	if (err || doc.length == 0) {
  		console.log('here');
  		res.send("Incorrect username/password!");
  	}

  	else {
  		bcrypt.compare(password, doc[0].password, function(err, resp) {
  			
  			if (resp == true) {
  				console.log('here 2');
  				res.send("Admin");
  			}
  			
  			else {
  				console.log('here 3');
  				res.send("Incorrect username/password!");
  			} 		
  		
  		});
  	  }
    });

  }

  else {
  	res.send("Not admin")
  }

});

module.exports = router;
