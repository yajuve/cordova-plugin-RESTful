---
title: Cordova Plugin RESTful 
description: Cordova plugin to use RESTful API.
version: 0.0.2
---

# Cordova Plugin RESTful 
* [Instalation](#instalation)
* [Supported platforms](#supported-platforms)
* [How to use](#how-to-use)
* [Full example Ionic2 / Angular2](#full-example-ionic2-angular2)

# Instalation
Open terminal
```shell
npm install cordova-plugin-restful
```

# Supported platforms
* Android 

# How to use
## Get

```javascript
cordova.plugins.RESTful.get(user, pass, url, (resp) => {
        console.log('Success call get');
    }, (err) => {
        // Handle error
        console.error(err)
    });
```

## Post

```javascript
cordova.plugins.RESTful.post(user, pass, url, body, (resp) => {
        console.log('Success send post');
    }, (err) => {
        // Handle error
        console.error(err)
    });
```
# Full example Ionic2 / Angular2

```javascript

import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

declare var cordova: any

@Component({
  selector: 'page-rest',
  templateUrl: 'rest.html'
})
export class RestPage {

  private user: string = 'yourUser';
  private pass: string = 'yourPass';
  private url : string = 'http://example.com/rest';

  constructor(public navCtrl: NavController, public navParams: NavParams)
  { }

  get() {
    cordova.plugins.RESTful.get(this.user, this.pass, this.url, (resp) => {
        console.log('Success call get');
    }, (err) => {
        // Handle error
        console.error(err)
    });
  }

}

```