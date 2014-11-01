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

module.exports = router;
