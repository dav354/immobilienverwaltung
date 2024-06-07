import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.ts';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '7d0cf3a6d572d46581b246fca94bf649d906b044033d1bcc982dba9605bcc573') {
    pending.push(import('./chunks/chunk-ef066201558f39b564c99db6e37a3fc00ea1afcc2c300321d4b7e667ae0b9dfe.js'));
  }
  if (key === '01e3f12b7cb61b74f3789a12e201603cd55afa41b501d3c442573a042978ecbf') {
    pending.push(import('./chunks/chunk-feb48d6c5519cb680252ad804f40593cab4dae24585cd68cd04bd9e92a9b87f7.js'));
  }
  if (key === '56b5976fb3cf18e0080934bebdee4fd18b17be02d7e2fc1c3c00dd0b35056b0e') {
    pending.push(import('./chunks/chunk-5f000db21b0374c1def7c5dcef9427326151a7d507f699f73331eb53e416518a.js'));
  }
  if (key === '3ef080ee97a9b425fc5f70dc4b9d2fe09fdbc97075b679ce46904a67816126ec') {
    pending.push(import('./chunks/chunk-ef066201558f39b564c99db6e37a3fc00ea1afcc2c300321d4b7e667ae0b9dfe.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}