// Browser shim for CommonJS packages that reference Node's global object.
(globalThis as { global?: typeof globalThis }).global ??= globalThis;

import 'zone.js';
