import { Component } from '@angular/core';
import { LoginService } from '../../../services/login/login.service';
import { Router } from '@angular/router';
import { ConfirmBoxInitializer, DialogLayoutDisplay, AppearanceAnimation, DisappearanceAnimation } from '@costlydeveloper/ngx-awesome-popup';
import { tap } from 'rxjs';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css'],
})
export class SideBarComponent {
  constructor(private loginService: LoginService, private router: Router) {}
  
  logout() {

    const newConfirmBox = new ConfirmBoxInitializer();

    newConfirmBox.setTitle('Confirm Logout!');
    newConfirmBox.setMessage('Are you sure you want to logout?');

    newConfirmBox.setConfig({
    layoutType: DialogLayoutDisplay.WARNING,
    animationIn: AppearanceAnimation.BOUNCE_IN, 
    animationOut: DisappearanceAnimation.BOUNCE_OUT,
    allowHtmlMessage: true,
    buttonPosition: 'center',
    });

    newConfirmBox.setButtonLabels('Yes', 'No');

    newConfirmBox.openConfirmBox$()
    .pipe(
      tap(value => {
        if(value.success){
          this.loginService.logout();
          this.router.navigate(['login']);
        }
      })
    )
    .subscribe();
  }
}
