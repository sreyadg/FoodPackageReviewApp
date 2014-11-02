var express = require('express');
var router = express.Router();
var http = require("http");
var bcrypt = require('bcrypt-nodejs');

/* GET users listing. */
router.post('/', function(req, res) {

	var username = req.body.username;
	var _id = req.body._id;

	// Set our internal DB variable
	var db = req.db;

	// Set our collection
	var collection = db.get('users');

	if (_id == undefined) {

		collection.find( { "username" : username }, {}, function (err, doc) {
		console.log('Sending data...')
		res.send(doc);

	 });
	}

	else {
		collection.find( { "_id" : _id }, {}, function (err, doc) {
		console.log('Sending data...')
		res.send(doc);
	 });
	}	
});

router.post('/create', function(req, res) {

	var username = req.body.username;
	var first_name = req.body.first_name;
	var last_name = req.body.last_name;
	var dob = req.body.dob;
	var email = req.body.email;
	var password = req.body.password;


	// Set our internal DB variable
	var db = req.db;


	// Set our collection
	var collection = db.get('users');


	//salt password
	bcrypt.genSalt(10, function(err, salt) {
		bcrypt.hash(password, null, null, function(err, hash) {
			collection.insert({
				"username" : username,
				"first_name" : first_name,
				"last_name" : last_name,
				"email" : email,
				"dob" : dob,
				"password" : hash
			}, function (err, doc) {
				if (err) {
					console.log("There was a problem adding the information to the database.");
				}
				else {          
					console.log('Added user login details!');
				}
			});
		});
	});  
});

router.post('/update', function(req, res) {
  
  var _id = req.body._id;
  var first_name = req.body.first_name;
  var last_name = req.body.last_name;
  var dob = req.body.dob;
  var email = req.body.email;

  console.log(req.body);

  // Set our internal DB variable
  var db = req.db;

  // Set our collection
  var collection = db.get('users');

  if (req.body == null || req.body == undefined) {

  	res.send("No changes made!")
  
  }

  else {

  	collection.find( { "_id" : _id }, {}, function (err, doc) {

  	if (err) {
        res.send("There was a problem modifying the information to the database.");
      }

      else { 

      	console.log("entered");

      	collection.update({ "_id" : _id },
      		{ $set: { 
      			"first_name" : first_name ,
      			"last_name" : last_name ,
      			"dob" : dob ,
      			"email" : email
				}
			});
      	  res.send('Update successful!')
        }
    });


  }

   
});
 

module.exports = router;
