import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MyAppSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [MyAppSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [MyAppSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyAppSharedModule {
  static forRoot() {
    return {
      ngModule: MyAppSharedModule
    };
  }
}
