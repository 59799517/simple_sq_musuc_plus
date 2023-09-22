import{n as de,q as Gt,s as Ee,t as Pn,v as Fn,x as E,r as I,y as Je,z as Ot,A as b,B as In,V as Tt,C as Ze,D as Te,E as Mn,G as At,H as Bt,I as H,J as L,K as ae,L as rt,M as he,N as je,O as Bn,P as Ae,Q as qt,R as $n,S as te,T as Be,U as Yt,W as re,X as Pe,Y as Zt,Z as se,$ as En,a0 as Fe,a1 as Pt,a2 as et,a3 as An,a4 as Nn,a5 as Xt,a6 as ft,a7 as Ln,a8 as Hn,a9 as J,aa as Dn,ab as Kn,ac as Jt,ad as ge,ae as Ft,af as jn,ag as Qt,ah as Vn,F as Ke,ai as Wn,aj as Un,ak as Gn,al as qn,am as Yn,an as Zn,ao as Xn,ap as Jn,aq as Nt,ar as Qn,as as eo,at as to,_ as no,a as oo,d as ze,e as ne,w as oe,f as ro,g as ee,i as $e,au as ht,av as vt,aw as lo,l as io,o as pe,j as gt,k as xe,c as bt,p as ao,m as so}from"./index-2d7ac089.js";import{s as co,a as uo,m as fo,A as ho,b as vo}from"./api-142e8b1c.js";import{T as go}from"./TopWitge-7c110398.js";import{u as Lt}from"./use-merged-state-b12a1513.js";import{u as en,N as bo,_ as po}from"./Input-7e965a27.js";import{c as tn,a as Qe,b as mo,i as $t,d as yo,N as wo,u as Co,e as It,V as xo,f as ko,g as So,_ as _o}from"./Tooltip-afa8d11a.js";import{_ as Ro,a as zo,b as Oo}from"./Thing-661dac6c.js";import{_ as To}from"./Form-e83bc1d6.js";import"./context-48e04b95.js";function tt(e,n){let{target:t}=e;for(;t;){if(t.dataset&&t.dataset[n]!==void 0)return!0;t=t.parentElement}return!1}function Po(e){switch(typeof e){case"string":return e||void 0;case"number":return String(e);default:return}}function pt(e){const n=e.filter(t=>t!==void 0);if(n.length!==0)return n.length===1?n[0]:t=>{e.forEach(o=>{o&&o(t)})}}function Ht(e){return e&-e}class Fo{constructor(n,t){this.l=n,this.min=t;const o=new Array(n+1);for(let r=0;r<n+1;++r)o[r]=0;this.ft=o}add(n,t){if(t===0)return;const{l:o,ft:r}=this;for(n+=1;n<=o;)r[n]+=t,n+=Ht(n)}get(n){return this.sum(n+1)-this.sum(n)}sum(n){if(n===void 0&&(n=this.l),n<=0)return 0;const{ft:t,min:o,l:r}=this;if(n>r)throw new Error("[FinweckTree.sum]: `i` is larger than length.");let a=n*o;for(;n>0;)a+=t[n],n-=Ht(n);return a}getBound(n){let t=0,o=this.l;for(;o>t;){const r=Math.floor((t+o)/2),a=this.sum(r);if(a>n){o=r;continue}else if(a<n){if(t===r)return this.sum(t+1)<=n?t+1:r;t=r}else return r}return t}}let Xe;function Io(){return Xe===void 0&&("matchMedia"in window?Xe=window.matchMedia("(pointer:coarse)").matches:Xe=!1),Xe}let mt;function Dt(){return mt===void 0&&(mt="chrome"in window?window.devicePixelRatio:1),mt}const Mo=Qe(".v-vl",{maxHeight:"inherit",height:"100%",overflow:"auto",minWidth:"1px"},[Qe("&:not(.v-vl--show-scrollbar)",{scrollbarWidth:"none"},[Qe("&::-webkit-scrollbar, &::-webkit-scrollbar-track-piece, &::-webkit-scrollbar-thumb",{width:0,height:0,display:"none"})])]),Bo=de({name:"VirtualList",inheritAttrs:!1,props:{showScrollbar:{type:Boolean,default:!0},items:{type:Array,default:()=>[]},itemSize:{type:Number,required:!0},itemResizable:Boolean,itemsStyle:[String,Object],visibleItemsTag:{type:[String,Object],default:"div"},visibleItemsProps:Object,ignoreItemResize:Boolean,onScroll:Function,onWheel:Function,onResize:Function,defaultScrollKey:[Number,String],defaultScrollIndex:Number,keyField:{type:String,default:"key"},paddingTop:{type:[Number,String],default:0},paddingBottom:{type:[Number,String],default:0}},setup(e){const n=Gt();Mo.mount({id:"vueuc/virtual-list",head:!0,anchorMetaName:tn,ssr:n}),Ee(()=>{const{defaultScrollIndex:h,defaultScrollKey:k}=e;h!=null?u({index:h}):k!=null&&u({key:k})});let t=!1,o=!1;Pn(()=>{if(t=!1,!o){o=!0;return}u({top:g.value,left:v})}),Fn(()=>{t=!0,o||(o=!0)});const r=E(()=>{const h=new Map,{keyField:k}=e;return e.items.forEach((B,G)=>{h.set(B[k],G)}),h}),a=I(null),l=I(void 0),i=new Map,c=E(()=>{const{items:h,itemSize:k,keyField:B}=e,G=new Fo(h.length,k);return h.forEach((Z,Y)=>{const W=Z[B],X=i.get(W);X!==void 0&&G.add(Y,X)}),G}),d=I(0);let v=0;const g=I(0),_=Je(()=>Math.max(c.value.getBound(g.value-Ot(e.paddingTop))-1,0)),x=E(()=>{const{value:h}=l;if(h===void 0)return[];const{items:k,itemSize:B}=e,G=_.value,Z=Math.min(G+Math.ceil(h/B+1),k.length-1),Y=[];for(let W=G;W<=Z;++W)Y.push(k[W]);return Y}),u=(h,k)=>{if(typeof h=="number"){F(h,k,"auto");return}const{left:B,top:G,index:Z,key:Y,position:W,behavior:X,debounce:p=!0}=h;if(B!==void 0||G!==void 0)F(B,G,X);else if(Z!==void 0)y(Z,X,p);else if(Y!==void 0){const z=r.value.get(Y);z!==void 0&&y(z,X,p)}else W==="bottom"?F(0,Number.MAX_SAFE_INTEGER,X):W==="top"&&F(0,0,X)};let P,O=null;function y(h,k,B){const{value:G}=c,Z=G.sum(h)+Ot(e.paddingTop);if(!B)a.value.scrollTo({left:0,top:Z,behavior:k});else{P=h,O!==null&&window.clearTimeout(O),O=window.setTimeout(()=>{P=void 0,O=null},16);const{scrollTop:Y,offsetHeight:W}=a.value;if(Z>Y){const X=G.get(h);Z+X<=Y+W||a.value.scrollTo({left:0,top:Z+X-W,behavior:k})}else a.value.scrollTo({left:0,top:Z,behavior:k})}}function F(h,k,B){a.value.scrollTo({left:h,top:k,behavior:B})}function C(h,k){var B,G,Z;if(t||e.ignoreItemResize||A(k.target))return;const{value:Y}=c,W=r.value.get(h),X=Y.get(W),p=(Z=(G=(B=k.borderBoxSize)===null||B===void 0?void 0:B[0])===null||G===void 0?void 0:G.blockSize)!==null&&Z!==void 0?Z:k.contentRect.height;if(p===X)return;p-e.itemSize===0?i.delete(h):i.set(h,p-e.itemSize);const q=p-X;if(q===0)return;Y.add(W,q);const le=a.value;if(le!=null){if(P===void 0){const ue=Y.sum(W);le.scrollTop>ue&&le.scrollBy(0,q)}else if(W<P)le.scrollBy(0,q);else if(W===P){const ue=Y.sum(W);p+ue>le.scrollTop+le.offsetHeight&&le.scrollBy(0,q)}D()}d.value++}const f=!Io();let w=!1;function R(h){var k;(k=e.onScroll)===null||k===void 0||k.call(e,h),(!f||!w)&&D()}function M(h){var k;if((k=e.onWheel)===null||k===void 0||k.call(e,h),f){const B=a.value;if(B!=null){if(h.deltaX===0&&(B.scrollTop===0&&h.deltaY<=0||B.scrollTop+B.offsetHeight>=B.scrollHeight&&h.deltaY>=0))return;h.preventDefault(),B.scrollTop+=h.deltaY/Dt(),B.scrollLeft+=h.deltaX/Dt(),D(),w=!0,mo(()=>{w=!1})}}}function N(h){if(t||A(h.target)||h.contentRect.height===l.value)return;l.value=h.contentRect.height;const{onResize:k}=e;k!==void 0&&k(h)}function D(){const{value:h}=a;h!=null&&(g.value=h.scrollTop,v=h.scrollLeft)}function A(h){let k=h;for(;k!==null;){if(k.style.display==="none")return!0;k=k.parentElement}return!1}return{listHeight:l,listStyle:{overflow:"auto"},keyToIndex:r,itemsStyle:E(()=>{const{itemResizable:h}=e,k=Ze(c.value.sum());return d.value,[e.itemsStyle,{boxSizing:"content-box",height:h?"":k,minHeight:h?k:"",paddingTop:Ze(e.paddingTop),paddingBottom:Ze(e.paddingBottom)}]}),visibleItemsStyle:E(()=>(d.value,{transform:`translateY(${Ze(c.value.sum(_.value))})`})),viewportItems:x,listElRef:a,itemsElRef:I(null),scrollTo:u,handleListResize:N,handleListScroll:R,handleListWheel:M,handleItemResize:C}},render(){const{itemResizable:e,keyField:n,keyToIndex:t,visibleItemsTag:o}=this;return b(Tt,{onResize:this.handleListResize},{default:()=>{var r,a;return b("div",In(this.$attrs,{class:["v-vl",this.showScrollbar&&"v-vl--show-scrollbar"],onScroll:this.handleListScroll,onWheel:this.handleListWheel,ref:"listElRef"}),[this.items.length!==0?b("div",{ref:"itemsElRef",class:"v-vl-items",style:this.itemsStyle},[b(o,Object.assign({class:"v-vl-visible-items",style:this.visibleItemsStyle},this.visibleItemsProps),{default:()=>this.viewportItems.map(l=>{const i=l[n],c=t.get(i),d=this.$slots.default({item:l,index:c})[0];return e?b(Tt,{key:i,onResize:v=>this.handleItemResize(i,v)},{default:()=>d}):(d.key=i,d)})})]):(a=(r=this.$slots).empty)===null||a===void 0?void 0:a.call(r)])}})}}),Oe="v-hidden",$o=Qe("[v-hidden]",{display:"none!important"}),Kt=de({name:"Overflow",props:{getCounter:Function,getTail:Function,updateCounter:Function,onUpdateOverflow:Function},setup(e,{slots:n}){const t=I(null),o=I(null);function r(){const{value:l}=t,{getCounter:i,getTail:c}=e;let d;if(i!==void 0?d=i():d=o.value,!l||!d)return;d.hasAttribute(Oe)&&d.removeAttribute(Oe);const{children:v}=l,g=l.offsetWidth,_=[],x=n.tail?c==null?void 0:c():null;let u=x?x.offsetWidth:0,P=!1;const O=l.children.length-(n.tail?1:0);for(let F=0;F<O-1;++F){if(F<0)continue;const C=v[F];if(P){C.hasAttribute(Oe)||C.setAttribute(Oe,"");continue}else C.hasAttribute(Oe)&&C.removeAttribute(Oe);const f=C.offsetWidth;if(u+=f,_[F]=f,u>g){const{updateCounter:w}=e;for(let R=F;R>=0;--R){const M=O-1-R;w!==void 0?w(M):d.textContent=`${M}`;const N=d.offsetWidth;if(u-=_[R],u+N<=g||R===0){P=!0,F=R-1,x&&(F===-1?(x.style.maxWidth=`${g-N}px`,x.style.boxSizing="border-box"):x.style.maxWidth="");break}}}}const{onUpdateOverflow:y}=e;P?y!==void 0&&y(!0):(y!==void 0&&y(!1),d.setAttribute(Oe,""))}const a=Gt();return $o.mount({id:"vueuc/overflow",head:!0,anchorMetaName:tn,ssr:a}),Ee(r),{selfRef:t,counterRef:o,sync:r}},render(){const{$slots:e}=this;return Te(this.sync),b("div",{class:"v-overflow",ref:"selfRef"},[Mn(e,"default"),e.counter?e.counter():b("span",{style:{display:"inline-block"},ref:"counterRef"}),e.tail?e.tail():null])}});function nn(e,n){n&&(Ee(()=>{const{value:t}=e;t&&At.registerHandler(t,n)}),Bt(()=>{const{value:t}=e;t&&At.unregisterHandler(t)}))}const Eo=de({name:"Checkmark",render(){return b("svg",{xmlns:"http://www.w3.org/2000/svg",viewBox:"0 0 16 16"},b("g",{fill:"none"},b("path",{d:"M14.046 3.486a.75.75 0 0 1-.032 1.06l-7.93 7.474a.85.85 0 0 1-1.188-.022l-2.68-2.72a.75.75 0 1 1 1.068-1.053l2.234 2.267l7.468-7.038a.75.75 0 0 1 1.06.032z",fill:"currentColor"})))}}),Ao=de({name:"Empty",render(){return b("svg",{viewBox:"0 0 28 28",fill:"none",xmlns:"http://www.w3.org/2000/svg"},b("path",{d:"M26 7.5C26 11.0899 23.0899 14 19.5 14C15.9101 14 13 11.0899 13 7.5C13 3.91015 15.9101 1 19.5 1C23.0899 1 26 3.91015 26 7.5ZM16.8536 4.14645C16.6583 3.95118 16.3417 3.95118 16.1464 4.14645C15.9512 4.34171 15.9512 4.65829 16.1464 4.85355L18.7929 7.5L16.1464 10.1464C15.9512 10.3417 15.9512 10.6583 16.1464 10.8536C16.3417 11.0488 16.6583 11.0488 16.8536 10.8536L19.5 8.20711L22.1464 10.8536C22.3417 11.0488 22.6583 11.0488 22.8536 10.8536C23.0488 10.6583 23.0488 10.3417 22.8536 10.1464L20.2071 7.5L22.8536 4.85355C23.0488 4.65829 23.0488 4.34171 22.8536 4.14645C22.6583 3.95118 22.3417 3.95118 22.1464 4.14645L19.5 6.79289L16.8536 4.14645Z",fill:"currentColor"}),b("path",{d:"M25 22.75V12.5991C24.5572 13.0765 24.053 13.4961 23.5 13.8454V16H17.5L17.3982 16.0068C17.0322 16.0565 16.75 16.3703 16.75 16.75C16.75 18.2688 15.5188 19.5 14 19.5C12.4812 19.5 11.25 18.2688 11.25 16.75L11.2432 16.6482C11.1935 16.2822 10.8797 16 10.5 16H4.5V7.25C4.5 6.2835 5.2835 5.5 6.25 5.5H12.2696C12.4146 4.97463 12.6153 4.47237 12.865 4H6.25C4.45507 4 3 5.45507 3 7.25V22.75C3 24.5449 4.45507 26 6.25 26H21.75C23.5449 26 25 24.5449 25 22.75ZM4.5 22.75V17.5H9.81597L9.85751 17.7041C10.2905 19.5919 11.9808 21 14 21L14.215 20.9947C16.2095 20.8953 17.842 19.4209 18.184 17.5H23.5V22.75C23.5 23.7165 22.7165 24.5 21.75 24.5H6.25C5.2835 24.5 4.5 23.7165 4.5 22.75Z",fill:"currentColor"}))}}),No=de({props:{onFocus:Function,onBlur:Function},setup(e){return()=>b("div",{style:"width: 0; height: 0",tabindex:0,onFocus:e.onFocus,onBlur:e.onBlur})}});function jt(e){return Array.isArray(e)?e:[e]}const Mt={STOP:"STOP"};function on(e,n){const t=n(e);e.children!==void 0&&t!==Mt.STOP&&e.children.forEach(o=>on(o,n))}function Lo(e,n={}){const{preserveGroup:t=!1}=n,o=[],r=t?l=>{l.isLeaf||(o.push(l.key),a(l.children))}:l=>{l.isLeaf||(l.isGroup||o.push(l.key),a(l.children))};function a(l){l.forEach(r)}return a(e),o}function Ho(e,n){const{isLeaf:t}=e;return t!==void 0?t:!n(e)}function Do(e){return e.children}function Ko(e){return e.key}function jo(){return!1}function Vo(e,n){const{isLeaf:t}=e;return!(t===!1&&!Array.isArray(n(e)))}function Wo(e){return e.disabled===!0}function Uo(e,n){return e.isLeaf===!1&&!Array.isArray(n(e))}function yt(e){var n;return e==null?[]:Array.isArray(e)?e:(n=e.checkedKeys)!==null&&n!==void 0?n:[]}function wt(e){var n;return e==null||Array.isArray(e)?[]:(n=e.indeterminateKeys)!==null&&n!==void 0?n:[]}function Go(e,n){const t=new Set(e);return n.forEach(o=>{t.has(o)||t.add(o)}),Array.from(t)}function qo(e,n){const t=new Set(e);return n.forEach(o=>{t.has(o)&&t.delete(o)}),Array.from(t)}function Yo(e){return(e==null?void 0:e.type)==="group"}function Zo(e){const n=new Map;return e.forEach((t,o)=>{n.set(t.key,o)}),t=>{var o;return(o=n.get(t))!==null&&o!==void 0?o:null}}class Xo extends Error{constructor(){super(),this.message="SubtreeNotLoadedError: checking a subtree whose required nodes are not fully loaded."}}function Jo(e,n,t,o){return nt(n.concat(e),t,o,!1)}function Qo(e,n){const t=new Set;return e.forEach(o=>{const r=n.treeNodeMap.get(o);if(r!==void 0){let a=r.parent;for(;a!==null&&!(a.disabled||t.has(a.key));)t.add(a.key),a=a.parent}}),t}function er(e,n,t,o){const r=nt(n,t,o,!1),a=nt(e,t,o,!0),l=Qo(e,t),i=[];return r.forEach(c=>{(a.has(c)||l.has(c))&&i.push(c)}),i.forEach(c=>r.delete(c)),r}function Ct(e,n){const{checkedKeys:t,keysToCheck:o,keysToUncheck:r,indeterminateKeys:a,cascade:l,leafOnly:i,checkStrategy:c,allowNotLoaded:d}=e;if(!l)return o!==void 0?{checkedKeys:Go(t,o),indeterminateKeys:Array.from(a)}:r!==void 0?{checkedKeys:qo(t,r),indeterminateKeys:Array.from(a)}:{checkedKeys:Array.from(t),indeterminateKeys:Array.from(a)};const{levelTreeNodeMap:v}=n;let g;r!==void 0?g=er(r,t,n,d):o!==void 0?g=Jo(o,t,n,d):g=nt(t,n,d,!1);const _=c==="parent",x=c==="child"||i,u=g,P=new Set,O=Math.max.apply(null,Array.from(v.keys()));for(let y=O;y>=0;y-=1){const F=y===0,C=v.get(y);for(const f of C){if(f.isLeaf)continue;const{key:w,shallowLoaded:R}=f;if(x&&R&&f.children.forEach(A=>{!A.disabled&&!A.isLeaf&&A.shallowLoaded&&u.has(A.key)&&u.delete(A.key)}),f.disabled||!R)continue;let M=!0,N=!1,D=!0;for(const A of f.children){const h=A.key;if(!A.disabled){if(D&&(D=!1),u.has(h))N=!0;else if(P.has(h)){N=!0,M=!1;break}else if(M=!1,N)break}}M&&!D?(_&&f.children.forEach(A=>{!A.disabled&&u.has(A.key)&&u.delete(A.key)}),u.add(w)):N&&P.add(w),F&&x&&u.has(w)&&u.delete(w)}}return{checkedKeys:Array.from(u),indeterminateKeys:Array.from(P)}}function nt(e,n,t,o){const{treeNodeMap:r,getChildren:a}=n,l=new Set,i=new Set(e);return e.forEach(c=>{const d=r.get(c);d!==void 0&&on(d,v=>{if(v.disabled)return Mt.STOP;const{key:g}=v;if(!l.has(g)&&(l.add(g),i.add(g),Uo(v.rawNode,a))){if(o)return Mt.STOP;if(!t)throw new Xo}})}),i}function tr(e,{includeGroup:n=!1,includeSelf:t=!0},o){var r;const a=o.treeNodeMap;let l=e==null?null:(r=a.get(e))!==null&&r!==void 0?r:null;const i={keyPath:[],treeNodePath:[],treeNode:l};if(l!=null&&l.ignored)return i.treeNode=null,i;for(;l;)!l.ignored&&(n||!l.isGroup)&&i.treeNodePath.push(l),l=l.parent;return i.treeNodePath.reverse(),t||i.treeNodePath.pop(),i.keyPath=i.treeNodePath.map(c=>c.key),i}function nr(e){if(e.length===0)return null;const n=e[0];return n.isGroup||n.ignored||n.disabled?n.getNext():n}function or(e,n){const t=e.siblings,o=t.length,{index:r}=e;return n?t[(r+1)%o]:r===t.length-1?null:t[r+1]}function Vt(e,n,{loop:t=!1,includeDisabled:o=!1}={}){const r=n==="prev"?rr:or,a={reverse:n==="prev"};let l=!1,i=null;function c(d){if(d!==null){if(d===e){if(!l)l=!0;else if(!e.disabled&&!e.isGroup){i=e;return}}else if((!d.disabled||o)&&!d.ignored&&!d.isGroup){i=d;return}if(d.isGroup){const v=Et(d,a);v!==null?i=v:c(r(d,t))}else{const v=r(d,!1);if(v!==null)c(v);else{const g=lr(d);g!=null&&g.isGroup?c(r(g,t)):t&&c(r(d,!0))}}}}return c(e),i}function rr(e,n){const t=e.siblings,o=t.length,{index:r}=e;return n?t[(r-1+o)%o]:r===0?null:t[r-1]}function lr(e){return e.parent}function Et(e,n={}){const{reverse:t=!1}=n,{children:o}=e;if(o){const{length:r}=o,a=t?r-1:0,l=t?-1:r,i=t?-1:1;for(let c=a;c!==l;c+=i){const d=o[c];if(!d.disabled&&!d.ignored)if(d.isGroup){const v=Et(d,n);if(v!==null)return v}else return d}}return null}const ir={getChild(){return this.ignored?null:Et(this)},getParent(){const{parent:e}=this;return e!=null&&e.isGroup?e.getParent():e},getNext(e={}){return Vt(this,"next",e)},getPrev(e={}){return Vt(this,"prev",e)}};function ar(e,n){const t=n?new Set(n):void 0,o=[];function r(a){a.forEach(l=>{o.push(l),!(l.isLeaf||!l.children||l.ignored)&&(l.isGroup||t===void 0||t.has(l.key))&&r(l.children)})}return r(e),o}function sr(e,n){const t=e.key;for(;n;){if(n.key===t)return!0;n=n.parent}return!1}function rn(e,n,t,o,r,a=null,l=0){const i=[];return e.forEach((c,d)=>{var v;const g=Object.create(o);if(g.rawNode=c,g.siblings=i,g.level=l,g.index=d,g.isFirstChild=d===0,g.isLastChild=d+1===e.length,g.parent=a,!g.ignored){const _=r(c);Array.isArray(_)&&(g.children=rn(_,n,t,o,r,g,l+1))}i.push(g),n.set(g.key,g),t.has(l)||t.set(l,[]),(v=t.get(l))===null||v===void 0||v.push(g)}),i}function cr(e,n={}){var t;const o=new Map,r=new Map,{getDisabled:a=Wo,getIgnored:l=jo,getIsGroup:i=Yo,getKey:c=Ko}=n,d=(t=n.getChildren)!==null&&t!==void 0?t:Do,v=n.ignoreEmptyChildren?f=>{const w=d(f);return Array.isArray(w)?w.length?w:null:w}:d,g=Object.assign({get key(){return c(this.rawNode)},get disabled(){return a(this.rawNode)},get isGroup(){return i(this.rawNode)},get isLeaf(){return Ho(this.rawNode,v)},get shallowLoaded(){return Vo(this.rawNode,v)},get ignored(){return l(this.rawNode)},contains(f){return sr(this,f)}},ir),_=rn(e,o,r,g,v);function x(f){if(f==null)return null;const w=o.get(f);return w&&!w.isGroup&&!w.ignored?w:null}function u(f){if(f==null)return null;const w=o.get(f);return w&&!w.ignored?w:null}function P(f,w){const R=u(f);return R?R.getPrev(w):null}function O(f,w){const R=u(f);return R?R.getNext(w):null}function y(f){const w=u(f);return w?w.getParent():null}function F(f){const w=u(f);return w?w.getChild():null}const C={treeNodes:_,treeNodeMap:o,levelTreeNodeMap:r,maxLevel:Math.max(...r.keys()),getChildren:v,getFlattenedNodes(f){return ar(_,f)},getNode:x,getPrev:P,getNext:O,getParent:y,getChild:F,getFirstAvailableNode(){return nr(_)},getPath(f,w={}){return tr(f,w,C)},getCheckedKeys(f,w={}){const{cascade:R=!0,leafOnly:M=!1,checkStrategy:N="all",allowNotLoaded:D=!1}=w;return Ct({checkedKeys:yt(f),indeterminateKeys:wt(f),cascade:R,leafOnly:M,checkStrategy:N,allowNotLoaded:D},C)},check(f,w,R={}){const{cascade:M=!0,leafOnly:N=!1,checkStrategy:D="all",allowNotLoaded:A=!1}=R;return Ct({checkedKeys:yt(w),indeterminateKeys:wt(w),keysToCheck:f==null?[]:jt(f),cascade:M,leafOnly:N,checkStrategy:D,allowNotLoaded:A},C)},uncheck(f,w,R={}){const{cascade:M=!0,leafOnly:N=!1,checkStrategy:D="all",allowNotLoaded:A=!1}=R;return Ct({checkedKeys:yt(w),indeterminateKeys:wt(w),keysToUncheck:f==null?[]:jt(f),cascade:M,leafOnly:N,checkStrategy:D,allowNotLoaded:A},C)},getNonLeafKeys(f={}){return Lo(_,f)}};return C}const dr=H("empty",`
 display: flex;
 flex-direction: column;
 align-items: center;
 font-size: var(--n-font-size);
`,[L("icon",`
 width: var(--n-icon-size);
 height: var(--n-icon-size);
 font-size: var(--n-icon-size);
 line-height: var(--n-icon-size);
 color: var(--n-icon-color);
 transition:
 color .3s var(--n-bezier);
 `,[ae("+",[L("description",`
 margin-top: 8px;
 `)])]),L("description",`
 transition: color .3s var(--n-bezier);
 color: var(--n-text-color);
 `),L("extra",`
 text-align: center;
 transition: color .3s var(--n-bezier);
 margin-top: 12px;
 color: var(--n-extra-text-color);
 `)]),ur=Object.assign(Object.assign({},he.props),{description:String,showDescription:{type:Boolean,default:!0},showIcon:{type:Boolean,default:!0},size:{type:String,default:"medium"},renderIcon:Function}),fr=de({name:"Empty",props:ur,setup(e){const{mergedClsPrefixRef:n,inlineThemeDisabled:t}=rt(e),o=he("Empty","-empty",dr,$n,e,n),{localeRef:r}=en("Empty"),a=je(Bn,null),l=E(()=>{var v,g,_;return(v=e.description)!==null&&v!==void 0?v:(_=(g=a==null?void 0:a.mergedComponentPropsRef.value)===null||g===void 0?void 0:g.Empty)===null||_===void 0?void 0:_.description}),i=E(()=>{var v,g;return((g=(v=a==null?void 0:a.mergedComponentPropsRef.value)===null||v===void 0?void 0:v.Empty)===null||g===void 0?void 0:g.renderIcon)||(()=>b(Ao,null))}),c=E(()=>{const{size:v}=e,{common:{cubicBezierEaseInOut:g},self:{[te("iconSize",v)]:_,[te("fontSize",v)]:x,textColor:u,iconColor:P,extraTextColor:O}}=o.value;return{"--n-icon-size":_,"--n-font-size":x,"--n-bezier":g,"--n-text-color":u,"--n-icon-color":P,"--n-extra-text-color":O}}),d=t?Ae("empty",E(()=>{let v="";const{size:g}=e;return v+=g[0],v}),c,e):void 0;return{mergedClsPrefix:n,mergedRenderIcon:i,localizedDescription:E(()=>l.value||r.value.description),cssVars:t?void 0:c,themeClass:d==null?void 0:d.themeClass,onRender:d==null?void 0:d.onRender}},render(){const{$slots:e,mergedClsPrefix:n,onRender:t}=this;return t==null||t(),b("div",{class:[`${n}-empty`,this.themeClass],style:this.cssVars},this.showIcon?b("div",{class:`${n}-empty__icon`},e.icon?e.icon():b(qt,{clsPrefix:n},{default:this.mergedRenderIcon})):null,this.showDescription?b("div",{class:`${n}-empty__description`},e.default?e.default():this.localizedDescription):null,e.extra?b("div",{class:`${n}-empty__extra`},e.extra()):null)}});function hr(e,n){return b(Yt,{name:"fade-in-scale-up-transition"},{default:()=>e?b(qt,{clsPrefix:n,class:`${n}-base-select-option__check`},{default:()=>b(Eo)}):null})}const Wt=de({name:"NBaseSelectOption",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(e){const{valueRef:n,pendingTmNodeRef:t,multipleRef:o,valueSetRef:r,renderLabelRef:a,renderOptionRef:l,labelFieldRef:i,valueFieldRef:c,showCheckmarkRef:d,nodePropsRef:v,handleOptionClick:g,handleOptionMouseEnter:_}=je($t),x=Je(()=>{const{value:y}=t;return y?e.tmNode.key===y.key:!1});function u(y){const{tmNode:F}=e;F.disabled||g(y,F)}function P(y){const{tmNode:F}=e;F.disabled||_(y,F)}function O(y){const{tmNode:F}=e,{value:C}=x;F.disabled||C||_(y,F)}return{multiple:o,isGrouped:Je(()=>{const{tmNode:y}=e,{parent:F}=y;return F&&F.rawNode.type==="group"}),showCheckmark:d,nodeProps:v,isPending:x,isSelected:Je(()=>{const{value:y}=n,{value:F}=o;if(y===null)return!1;const C=e.tmNode.rawNode[c.value];if(F){const{value:f}=r;return f.has(C)}else return y===C}),labelField:i,renderLabel:a,renderOption:l,handleMouseMove:O,handleMouseEnter:P,handleClick:u}},render(){const{clsPrefix:e,tmNode:{rawNode:n},isSelected:t,isPending:o,isGrouped:r,showCheckmark:a,nodeProps:l,renderOption:i,renderLabel:c,handleClick:d,handleMouseEnter:v,handleMouseMove:g}=this,_=hr(t,e),x=c?[c(n,t),a&&_]:[Be(n[this.labelField],n,t),a&&_],u=l==null?void 0:l(n),P=b("div",Object.assign({},u,{class:[`${e}-base-select-option`,n.class,u==null?void 0:u.class,{[`${e}-base-select-option--disabled`]:n.disabled,[`${e}-base-select-option--selected`]:t,[`${e}-base-select-option--grouped`]:r,[`${e}-base-select-option--pending`]:o,[`${e}-base-select-option--show-checkmark`]:a}],style:[(u==null?void 0:u.style)||"",n.style||""],onClick:pt([d,u==null?void 0:u.onClick]),onMouseenter:pt([v,u==null?void 0:u.onMouseenter]),onMousemove:pt([g,u==null?void 0:u.onMousemove])}),b("div",{class:`${e}-base-select-option__content`},x));return n.render?n.render({node:P,option:n,selected:t}):i?i({node:P,option:n,selected:t}):P}}),Ut=de({name:"NBaseSelectGroupHeader",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(){const{renderLabelRef:e,renderOptionRef:n,labelFieldRef:t,nodePropsRef:o}=je($t);return{labelField:t,nodeProps:o,renderLabel:e,renderOption:n}},render(){const{clsPrefix:e,renderLabel:n,renderOption:t,nodeProps:o,tmNode:{rawNode:r}}=this,a=o==null?void 0:o(r),l=n?n(r,!1):Be(r[this.labelField],r,!1),i=b("div",Object.assign({},a,{class:[`${e}-base-select-group-header`,a==null?void 0:a.class]}),l);return r.render?r.render({node:i,option:r}):t?t({node:i,option:r,selected:!1}):i}}),vr=H("base-select-menu",`
 line-height: 1.5;
 outline: none;
 z-index: 0;
 position: relative;
 border-radius: var(--n-border-radius);
 transition:
 background-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier);
 background-color: var(--n-color);
`,[H("scrollbar",`
 max-height: var(--n-height);
 `),H("virtual-list",`
 max-height: var(--n-height);
 `),H("base-select-option",`
 min-height: var(--n-option-height);
 font-size: var(--n-option-font-size);
 display: flex;
 align-items: center;
 `,[L("content",`
 z-index: 1;
 white-space: nowrap;
 text-overflow: ellipsis;
 overflow: hidden;
 `)]),H("base-select-group-header",`
 min-height: var(--n-option-height);
 font-size: .93em;
 display: flex;
 align-items: center;
 `),H("base-select-menu-option-wrapper",`
 position: relative;
 width: 100%;
 `),L("loading, empty",`
 display: flex;
 padding: 12px 32px;
 flex: 1;
 justify-content: center;
 `),L("loading",`
 color: var(--n-loading-color);
 font-size: var(--n-loading-size);
 `),L("action",`
 padding: 8px var(--n-option-padding-left);
 font-size: var(--n-option-font-size);
 transition: 
 color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 border-top: 1px solid var(--n-action-divider-color);
 color: var(--n-action-text-color);
 `),H("base-select-group-header",`
 position: relative;
 cursor: default;
 padding: var(--n-option-padding);
 color: var(--n-group-header-text-color);
 `),H("base-select-option",`
 cursor: pointer;
 position: relative;
 padding: var(--n-option-padding);
 transition:
 color .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 box-sizing: border-box;
 color: var(--n-option-text-color);
 opacity: 1;
 `,[re("show-checkmark",`
 padding-right: calc(var(--n-option-padding-right) + 20px);
 `),ae("&::before",`
 content: "";
 position: absolute;
 left: 4px;
 right: 4px;
 top: 0;
 bottom: 0;
 border-radius: var(--n-border-radius);
 transition: background-color .3s var(--n-bezier);
 `),ae("&:active",`
 color: var(--n-option-text-color-pressed);
 `),re("grouped",`
 padding-left: calc(var(--n-option-padding-left) * 1.5);
 `),re("pending",[ae("&::before",`
 background-color: var(--n-option-color-pending);
 `)]),re("selected",`
 color: var(--n-option-text-color-active);
 `,[ae("&::before",`
 background-color: var(--n-option-color-active);
 `),re("pending",[ae("&::before",`
 background-color: var(--n-option-color-active-pending);
 `)])]),re("disabled",`
 cursor: not-allowed;
 `,[Pe("selected",`
 color: var(--n-option-text-color-disabled);
 `),re("selected",`
 opacity: var(--n-option-opacity-disabled);
 `)]),L("check",`
 font-size: 16px;
 position: absolute;
 right: calc(var(--n-option-padding-right) - 4px);
 top: calc(50% - 7px);
 color: var(--n-option-check-color);
 transition: color .3s var(--n-bezier);
 `,[Zt({enterScale:"0.5"})])])]),gr=de({name:"InternalSelectMenu",props:Object.assign(Object.assign({},he.props),{clsPrefix:{type:String,required:!0},scrollable:{type:Boolean,default:!0},treeMate:{type:Object,required:!0},multiple:Boolean,size:{type:String,default:"medium"},value:{type:[String,Number,Array],default:null},autoPending:Boolean,virtualScroll:{type:Boolean,default:!0},show:{type:Boolean,default:!0},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},loading:Boolean,focusable:Boolean,renderLabel:Function,renderOption:Function,nodeProps:Function,showCheckmark:{type:Boolean,default:!0},onMousedown:Function,onScroll:Function,onFocus:Function,onBlur:Function,onKeyup:Function,onKeydown:Function,onTabOut:Function,onMouseenter:Function,onMouseleave:Function,onResize:Function,resetMenuOnOptionsChange:{type:Boolean,default:!0},inlineThemeDisabled:Boolean,onToggle:Function}),setup(e){const n=he("InternalSelectMenu","-internal-select-menu",vr,En,e,se(e,"clsPrefix")),t=I(null),o=I(null),r=I(null),a=E(()=>e.treeMate.getFlattenedNodes()),l=E(()=>Zo(a.value)),i=I(null);function c(){const{treeMate:p}=e;let z=null;const{value:q}=e;q===null?z=p.getFirstAvailableNode():(e.multiple?z=p.getNode((q||[])[(q||[]).length-1]):z=p.getNode(q),(!z||z.disabled)&&(z=p.getFirstAvailableNode())),h(z||null)}function d(){const{value:p}=i;p&&!e.treeMate.getNode(p.key)&&(i.value=null)}let v;Fe(()=>e.show,p=>{p?v=Fe(()=>e.treeMate,()=>{e.resetMenuOnOptionsChange?(e.autoPending?c():d(),Te(k)):d()},{immediate:!0}):v==null||v()},{immediate:!0}),Bt(()=>{v==null||v()});const g=E(()=>Ot(n.value.self[te("optionHeight",e.size)])),_=E(()=>ft(n.value.self[te("padding",e.size)])),x=E(()=>e.multiple&&Array.isArray(e.value)?new Set(e.value):new Set),u=E(()=>{const p=a.value;return p&&p.length===0});function P(p){const{onToggle:z}=e;z&&z(p)}function O(p){const{onScroll:z}=e;z&&z(p)}function y(p){var z;(z=r.value)===null||z===void 0||z.sync(),O(p)}function F(){var p;(p=r.value)===null||p===void 0||p.sync()}function C(){const{value:p}=i;return p||null}function f(p,z){z.disabled||h(z,!1)}function w(p,z){z.disabled||P(z)}function R(p){var z;tt(p,"action")||(z=e.onKeyup)===null||z===void 0||z.call(e,p)}function M(p){var z;tt(p,"action")||(z=e.onKeydown)===null||z===void 0||z.call(e,p)}function N(p){var z;(z=e.onMousedown)===null||z===void 0||z.call(e,p),!e.focusable&&p.preventDefault()}function D(){const{value:p}=i;p&&h(p.getNext({loop:!0}),!0)}function A(){const{value:p}=i;p&&h(p.getPrev({loop:!0}),!0)}function h(p,z=!1){i.value=p,z&&k()}function k(){var p,z;const q=i.value;if(!q)return;const le=l.value(q.key);le!==null&&(e.virtualScroll?(p=o.value)===null||p===void 0||p.scrollTo({index:le}):(z=r.value)===null||z===void 0||z.scrollTo({index:le,elSize:g.value}))}function B(p){var z,q;!((z=t.value)===null||z===void 0)&&z.contains(p.target)&&((q=e.onFocus)===null||q===void 0||q.call(e,p))}function G(p){var z,q;!((z=t.value)===null||z===void 0)&&z.contains(p.relatedTarget)||(q=e.onBlur)===null||q===void 0||q.call(e,p)}Pt($t,{handleOptionMouseEnter:f,handleOptionClick:w,valueSetRef:x,pendingTmNodeRef:i,nodePropsRef:se(e,"nodeProps"),showCheckmarkRef:se(e,"showCheckmark"),multipleRef:se(e,"multiple"),valueRef:se(e,"value"),renderLabelRef:se(e,"renderLabel"),renderOptionRef:se(e,"renderOption"),labelFieldRef:se(e,"labelField"),valueFieldRef:se(e,"valueField")}),Pt(yo,t),Ee(()=>{const{value:p}=r;p&&p.sync()});const Z=E(()=>{const{size:p}=e,{common:{cubicBezierEaseInOut:z},self:{height:q,borderRadius:le,color:ue,groupHeaderTextColor:ke,actionDividerColor:Se,optionTextColorPressed:ye,optionTextColor:me,optionTextColorDisabled:ve,optionTextColorActive:ce,optionOpacityDisabled:we,optionCheckColor:be,actionTextColor:Ne,optionColorPending:_e,optionColorActive:Re,loadingColor:Le,loadingSize:He,optionColorActivePending:De,[te("optionFontSize",p)]:Ie,[te("optionHeight",p)]:Me,[te("optionPadding",p)]:fe}}=n.value;return{"--n-height":q,"--n-action-divider-color":Se,"--n-action-text-color":Ne,"--n-bezier":z,"--n-border-radius":le,"--n-color":ue,"--n-option-font-size":Ie,"--n-group-header-text-color":ke,"--n-option-check-color":be,"--n-option-color-pending":_e,"--n-option-color-active":Re,"--n-option-color-active-pending":De,"--n-option-height":Me,"--n-option-opacity-disabled":we,"--n-option-text-color":me,"--n-option-text-color-active":ce,"--n-option-text-color-disabled":ve,"--n-option-text-color-pressed":ye,"--n-option-padding":fe,"--n-option-padding-left":ft(fe,"left"),"--n-option-padding-right":ft(fe,"right"),"--n-loading-color":Le,"--n-loading-size":He}}),{inlineThemeDisabled:Y}=e,W=Y?Ae("internal-select-menu",E(()=>e.size[0]),Z,e):void 0,X={selfRef:t,next:D,prev:A,getPendingTmNode:C};return nn(t,e.onResize),Object.assign({mergedTheme:n,virtualListRef:o,scrollbarRef:r,itemSize:g,padding:_,flattenedNodes:a,empty:u,virtualListContainer(){const{value:p}=o;return p==null?void 0:p.listElRef},virtualListContent(){const{value:p}=o;return p==null?void 0:p.itemsElRef},doScroll:O,handleFocusin:B,handleFocusout:G,handleKeyUp:R,handleKeyDown:M,handleMouseDown:N,handleVirtualListResize:F,handleVirtualListScroll:y,cssVars:Y?void 0:Z,themeClass:W==null?void 0:W.themeClass,onRender:W==null?void 0:W.onRender},X)},render(){const{$slots:e,virtualScroll:n,clsPrefix:t,mergedTheme:o,themeClass:r,onRender:a}=this;return a==null||a(),b("div",{ref:"selfRef",tabindex:this.focusable?0:-1,class:[`${t}-base-select-menu`,r,this.multiple&&`${t}-base-select-menu--multiple`],style:this.cssVars,onFocusin:this.handleFocusin,onFocusout:this.handleFocusout,onKeyup:this.handleKeyUp,onKeydown:this.handleKeyDown,onMousedown:this.handleMouseDown,onMouseenter:this.onMouseenter,onMouseleave:this.onMouseleave},this.loading?b("div",{class:`${t}-base-select-menu__loading`},b(An,{clsPrefix:t,strokeWidth:20})):this.empty?b("div",{class:`${t}-base-select-menu__empty`,"data-empty":!0},Xt(e.empty,()=>[b(fr,{theme:o.peers.Empty,themeOverrides:o.peerOverrides.Empty})])):b(Nn,{ref:"scrollbarRef",theme:o.peers.Scrollbar,themeOverrides:o.peerOverrides.Scrollbar,scrollable:this.scrollable,container:n?this.virtualListContainer:void 0,content:n?this.virtualListContent:void 0,onScroll:n?void 0:this.doScroll},{default:()=>n?b(Bo,{ref:"virtualListRef",class:`${t}-virtual-list`,items:this.flattenedNodes,itemSize:this.itemSize,showScrollbar:!1,paddingTop:this.padding.top,paddingBottom:this.padding.bottom,onResize:this.handleVirtualListResize,onScroll:this.handleVirtualListScroll,itemResizable:!0},{default:({item:l})=>l.isGroup?b(Ut,{key:l.key,clsPrefix:t,tmNode:l}):l.ignored?null:b(Wt,{clsPrefix:t,key:l.key,tmNode:l})}):b("div",{class:`${t}-base-select-menu-option-wrapper`,style:{paddingTop:this.padding.top,paddingBottom:this.padding.bottom}},this.flattenedNodes.map(l=>l.isGroup?b(Ut,{key:l.key,clsPrefix:t,tmNode:l}):b(Wt,{clsPrefix:t,key:l.key,tmNode:l})))}),et(e.action,l=>l&&[b("div",{class:`${t}-base-select-menu__action`,"data-action":!0,key:"action"},l),b(No,{onFocus:this.onTabOut,key:"focus-detector"})]))}}),br=e=>{const{textColor2:n,primaryColorHover:t,primaryColorPressed:o,primaryColor:r,infoColor:a,successColor:l,warningColor:i,errorColor:c,baseColor:d,borderColor:v,opacityDisabled:g,tagColor:_,closeIconColor:x,closeIconColorHover:u,closeIconColorPressed:P,borderRadiusSmall:O,fontSizeMini:y,fontSizeTiny:F,fontSizeSmall:C,fontSizeMedium:f,heightMini:w,heightTiny:R,heightSmall:M,heightMedium:N,closeColorHover:D,closeColorPressed:A,buttonColor2Hover:h,buttonColor2Pressed:k,fontWeightStrong:B}=e;return Object.assign(Object.assign({},Hn),{closeBorderRadius:O,heightTiny:w,heightSmall:R,heightMedium:M,heightLarge:N,borderRadius:O,opacityDisabled:g,fontSizeTiny:y,fontSizeSmall:F,fontSizeMedium:C,fontSizeLarge:f,fontWeightStrong:B,textColorCheckable:n,textColorHoverCheckable:n,textColorPressedCheckable:n,textColorChecked:d,colorCheckable:"#0000",colorHoverCheckable:h,colorPressedCheckable:k,colorChecked:r,colorCheckedHover:t,colorCheckedPressed:o,border:`1px solid ${v}`,textColor:n,color:_,colorBordered:"rgb(250, 250, 252)",closeIconColor:x,closeIconColorHover:u,closeIconColorPressed:P,closeColorHover:D,closeColorPressed:A,borderPrimary:`1px solid ${J(r,{alpha:.3})}`,textColorPrimary:r,colorPrimary:J(r,{alpha:.12}),colorBorderedPrimary:J(r,{alpha:.1}),closeIconColorPrimary:r,closeIconColorHoverPrimary:r,closeIconColorPressedPrimary:r,closeColorHoverPrimary:J(r,{alpha:.12}),closeColorPressedPrimary:J(r,{alpha:.18}),borderInfo:`1px solid ${J(a,{alpha:.3})}`,textColorInfo:a,colorInfo:J(a,{alpha:.12}),colorBorderedInfo:J(a,{alpha:.1}),closeIconColorInfo:a,closeIconColorHoverInfo:a,closeIconColorPressedInfo:a,closeColorHoverInfo:J(a,{alpha:.12}),closeColorPressedInfo:J(a,{alpha:.18}),borderSuccess:`1px solid ${J(l,{alpha:.3})}`,textColorSuccess:l,colorSuccess:J(l,{alpha:.12}),colorBorderedSuccess:J(l,{alpha:.1}),closeIconColorSuccess:l,closeIconColorHoverSuccess:l,closeIconColorPressedSuccess:l,closeColorHoverSuccess:J(l,{alpha:.12}),closeColorPressedSuccess:J(l,{alpha:.18}),borderWarning:`1px solid ${J(i,{alpha:.35})}`,textColorWarning:i,colorWarning:J(i,{alpha:.15}),colorBorderedWarning:J(i,{alpha:.12}),closeIconColorWarning:i,closeIconColorHoverWarning:i,closeIconColorPressedWarning:i,closeColorHoverWarning:J(i,{alpha:.12}),closeColorPressedWarning:J(i,{alpha:.18}),borderError:`1px solid ${J(c,{alpha:.23})}`,textColorError:c,colorError:J(c,{alpha:.1}),colorBorderedError:J(c,{alpha:.08}),closeIconColorError:c,closeIconColorHoverError:c,closeIconColorPressedError:c,closeColorHoverError:J(c,{alpha:.12}),closeColorPressedError:J(c,{alpha:.18})})},pr={name:"Tag",common:Ln,self:br},mr=pr,yr={color:Object,type:{type:String,default:"default"},round:Boolean,size:{type:String,default:"medium"},closable:Boolean,disabled:{type:Boolean,default:void 0}},wr=H("tag",`
 white-space: nowrap;
 position: relative;
 box-sizing: border-box;
 cursor: default;
 display: inline-flex;
 align-items: center;
 flex-wrap: nowrap;
 padding: var(--n-padding);
 border-radius: var(--n-border-radius);
 color: var(--n-text-color);
 background-color: var(--n-color);
 transition: 
 border-color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 line-height: 1;
 height: var(--n-height);
 font-size: var(--n-font-size);
`,[re("strong",`
 font-weight: var(--n-font-weight-strong);
 `),L("border",`
 pointer-events: none;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 border-radius: inherit;
 border: var(--n-border);
 transition: border-color .3s var(--n-bezier);
 `),L("icon",`
 display: flex;
 margin: 0 4px 0 0;
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 font-size: var(--n-avatar-size-override);
 `),L("avatar",`
 display: flex;
 margin: 0 6px 0 0;
 `),L("close",`
 margin: var(--n-close-margin);
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `),re("round",`
 padding: 0 calc(var(--n-height) / 3);
 border-radius: calc(var(--n-height) / 2);
 `,[L("icon",`
 margin: 0 4px 0 calc((var(--n-height) - 8px) / -2);
 `),L("avatar",`
 margin: 0 6px 0 calc((var(--n-height) - 8px) / -2);
 `),re("closable",`
 padding: 0 calc(var(--n-height) / 4) 0 calc(var(--n-height) / 3);
 `)]),re("icon, avatar",[re("round",`
 padding: 0 calc(var(--n-height) / 3) 0 calc(var(--n-height) / 2);
 `)]),re("disabled",`
 cursor: not-allowed !important;
 opacity: var(--n-opacity-disabled);
 `),re("checkable",`
 cursor: pointer;
 box-shadow: none;
 color: var(--n-text-color-checkable);
 background-color: var(--n-color-checkable);
 `,[Pe("disabled",[ae("&:hover","background-color: var(--n-color-hover-checkable);",[Pe("checked","color: var(--n-text-color-hover-checkable);")]),ae("&:active","background-color: var(--n-color-pressed-checkable);",[Pe("checked","color: var(--n-text-color-pressed-checkable);")])]),re("checked",`
 color: var(--n-text-color-checked);
 background-color: var(--n-color-checked);
 `,[Pe("disabled",[ae("&:hover","background-color: var(--n-color-checked-hover);"),ae("&:active","background-color: var(--n-color-checked-pressed);")])])])]),Cr=Object.assign(Object.assign(Object.assign({},he.props),yr),{bordered:{type:Boolean,default:void 0},checked:Boolean,checkable:Boolean,strong:Boolean,triggerClickOnClose:Boolean,onClose:[Array,Function],onMouseenter:Function,onMouseleave:Function,"onUpdate:checked":Function,onUpdateChecked:Function,internalCloseFocusable:{type:Boolean,default:!0},internalCloseIsButtonTag:{type:Boolean,default:!0},onCheckedChange:Function}),ln=Jt("n-tag"),xt=de({name:"Tag",props:Cr,setup(e){const n=I(null),{mergedBorderedRef:t,mergedClsPrefixRef:o,inlineThemeDisabled:r,mergedRtlRef:a}=rt(e),l=he("Tag","-tag",wr,mr,e,o);Pt(ln,{roundRef:se(e,"round")});function i(x){if(!e.disabled&&e.checkable){const{checked:u,onCheckedChange:P,onUpdateChecked:O,"onUpdate:checked":y}=e;O&&O(!u),y&&y(!u),P&&P(!u)}}function c(x){if(e.triggerClickOnClose||x.stopPropagation(),!e.disabled){const{onClose:u}=e;u&&ge(u,x)}}const d={setTextContent(x){const{value:u}=n;u&&(u.textContent=x)}},v=Dn("Tag",a,o),g=E(()=>{const{type:x,size:u,color:{color:P,textColor:O}={}}=e,{common:{cubicBezierEaseInOut:y},self:{padding:F,closeMargin:C,closeMarginRtl:f,borderRadius:w,opacityDisabled:R,textColorCheckable:M,textColorHoverCheckable:N,textColorPressedCheckable:D,textColorChecked:A,colorCheckable:h,colorHoverCheckable:k,colorPressedCheckable:B,colorChecked:G,colorCheckedHover:Z,colorCheckedPressed:Y,closeBorderRadius:W,fontWeightStrong:X,[te("colorBordered",x)]:p,[te("closeSize",u)]:z,[te("closeIconSize",u)]:q,[te("fontSize",u)]:le,[te("height",u)]:ue,[te("color",x)]:ke,[te("textColor",x)]:Se,[te("border",x)]:ye,[te("closeIconColor",x)]:me,[te("closeIconColorHover",x)]:ve,[te("closeIconColorPressed",x)]:ce,[te("closeColorHover",x)]:we,[te("closeColorPressed",x)]:be}}=l.value;return{"--n-font-weight-strong":X,"--n-avatar-size-override":`calc(${ue} - 8px)`,"--n-bezier":y,"--n-border-radius":w,"--n-border":ye,"--n-close-icon-size":q,"--n-close-color-pressed":be,"--n-close-color-hover":we,"--n-close-border-radius":W,"--n-close-icon-color":me,"--n-close-icon-color-hover":ve,"--n-close-icon-color-pressed":ce,"--n-close-icon-color-disabled":me,"--n-close-margin":C,"--n-close-margin-rtl":f,"--n-close-size":z,"--n-color":P||(t.value?p:ke),"--n-color-checkable":h,"--n-color-checked":G,"--n-color-checked-hover":Z,"--n-color-checked-pressed":Y,"--n-color-hover-checkable":k,"--n-color-pressed-checkable":B,"--n-font-size":le,"--n-height":ue,"--n-opacity-disabled":R,"--n-padding":F,"--n-text-color":O||Se,"--n-text-color-checkable":M,"--n-text-color-checked":A,"--n-text-color-hover-checkable":N,"--n-text-color-pressed-checkable":D}}),_=r?Ae("tag",E(()=>{let x="";const{type:u,size:P,color:{color:O,textColor:y}={}}=e;return x+=u[0],x+=P[0],O&&(x+=`a${Ft(O)}`),y&&(x+=`b${Ft(y)}`),t.value&&(x+="c"),x}),g,e):void 0;return Object.assign(Object.assign({},d),{rtlEnabled:v,mergedClsPrefix:o,contentRef:n,mergedBordered:t,handleClick:i,handleCloseClick:c,cssVars:r?void 0:g,themeClass:_==null?void 0:_.themeClass,onRender:_==null?void 0:_.onRender})},render(){var e,n;const{mergedClsPrefix:t,rtlEnabled:o,closable:r,color:{borderColor:a}={},round:l,onRender:i,$slots:c}=this;i==null||i();const d=et(c.avatar,g=>g&&b("div",{class:`${t}-tag__avatar`},g)),v=et(c.icon,g=>g&&b("div",{class:`${t}-tag__icon`},g));return b("div",{class:[`${t}-tag`,this.themeClass,{[`${t}-tag--rtl`]:o,[`${t}-tag--strong`]:this.strong,[`${t}-tag--disabled`]:this.disabled,[`${t}-tag--checkable`]:this.checkable,[`${t}-tag--checked`]:this.checkable&&this.checked,[`${t}-tag--round`]:l,[`${t}-tag--avatar`]:d,[`${t}-tag--icon`]:v,[`${t}-tag--closable`]:r}],style:this.cssVars,onClick:this.handleClick,onMouseenter:this.onMouseenter,onMouseleave:this.onMouseleave},v||d,b("span",{class:`${t}-tag__content`,ref:"contentRef"},(n=(e=this.$slots).default)===null||n===void 0?void 0:n.call(e)),!this.checkable&&r?b(Kn,{clsPrefix:t,class:`${t}-tag__close`,disabled:this.disabled,onClick:this.handleCloseClick,focusable:this.internalCloseFocusable,round:l,isButtonTag:this.internalCloseIsButtonTag,absolute:!0}):null,!this.checkable&&this.mergedBordered?b("div",{class:`${t}-tag__border`,style:{borderColor:a}}):null)}}),xr=ae([H("base-selection",`
 position: relative;
 z-index: auto;
 box-shadow: none;
 width: 100%;
 max-width: 100%;
 display: inline-block;
 vertical-align: bottom;
 border-radius: var(--n-border-radius);
 min-height: var(--n-height);
 line-height: 1.5;
 font-size: var(--n-font-size);
 `,[H("base-loading",`
 color: var(--n-loading-color);
 `),H("base-selection-tags","min-height: var(--n-height);"),L("border, state-border",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 pointer-events: none;
 border: var(--n-border);
 border-radius: inherit;
 transition:
 box-shadow .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 `),L("state-border",`
 z-index: 1;
 border-color: #0000;
 `),H("base-suffix",`
 cursor: pointer;
 position: absolute;
 top: 50%;
 transform: translateY(-50%);
 right: 10px;
 `,[L("arrow",`
 font-size: var(--n-arrow-size);
 color: var(--n-arrow-color);
 transition: color .3s var(--n-bezier);
 `)]),H("base-selection-overlay",`
 display: flex;
 align-items: center;
 white-space: nowrap;
 pointer-events: none;
 position: absolute;
 top: 0;
 right: 0;
 bottom: 0;
 left: 0;
 padding: var(--n-padding-single);
 transition: color .3s var(--n-bezier);
 `,[L("wrapper",`
 flex-basis: 0;
 flex-grow: 1;
 overflow: hidden;
 text-overflow: ellipsis;
 `)]),H("base-selection-placeholder",`
 color: var(--n-placeholder-color);
 `,[L("inner",`
 max-width: 100%;
 overflow: hidden;
 `)]),H("base-selection-tags",`
 cursor: pointer;
 outline: none;
 box-sizing: border-box;
 position: relative;
 z-index: auto;
 display: flex;
 padding: var(--n-padding-multiple);
 flex-wrap: wrap;
 align-items: center;
 width: 100%;
 vertical-align: bottom;
 background-color: var(--n-color);
 border-radius: inherit;
 transition:
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 `),H("base-selection-label",`
 height: var(--n-height);
 display: inline-flex;
 width: 100%;
 vertical-align: bottom;
 cursor: pointer;
 outline: none;
 z-index: auto;
 box-sizing: border-box;
 position: relative;
 transition:
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 border-radius: inherit;
 background-color: var(--n-color);
 align-items: center;
 `,[H("base-selection-input",`
 font-size: inherit;
 line-height: inherit;
 outline: none;
 cursor: pointer;
 box-sizing: border-box;
 border:none;
 width: 100%;
 padding: var(--n-padding-single);
 background-color: #0000;
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 caret-color: var(--n-caret-color);
 `,[L("content",`
 text-overflow: ellipsis;
 overflow: hidden;
 white-space: nowrap; 
 `)]),L("render-label",`
 color: var(--n-text-color);
 `)]),Pe("disabled",[ae("&:hover",[L("state-border",`
 box-shadow: var(--n-box-shadow-hover);
 border: var(--n-border-hover);
 `)]),re("focus",[L("state-border",`
 box-shadow: var(--n-box-shadow-focus);
 border: var(--n-border-focus);
 `)]),re("active",[L("state-border",`
 box-shadow: var(--n-box-shadow-active);
 border: var(--n-border-active);
 `),H("base-selection-label","background-color: var(--n-color-active);"),H("base-selection-tags","background-color: var(--n-color-active);")])]),re("disabled","cursor: not-allowed;",[L("arrow",`
 color: var(--n-arrow-color-disabled);
 `),H("base-selection-label",`
 cursor: not-allowed;
 background-color: var(--n-color-disabled);
 `,[H("base-selection-input",`
 cursor: not-allowed;
 color: var(--n-text-color-disabled);
 `),L("render-label",`
 color: var(--n-text-color-disabled);
 `)]),H("base-selection-tags",`
 cursor: not-allowed;
 background-color: var(--n-color-disabled);
 `),H("base-selection-placeholder",`
 cursor: not-allowed;
 color: var(--n-placeholder-color-disabled);
 `)]),H("base-selection-input-tag",`
 height: calc(var(--n-height) - 6px);
 line-height: calc(var(--n-height) - 6px);
 outline: none;
 display: none;
 position: relative;
 margin-bottom: 3px;
 max-width: 100%;
 vertical-align: bottom;
 `,[L("input",`
 font-size: inherit;
 font-family: inherit;
 min-width: 1px;
 padding: 0;
 background-color: #0000;
 outline: none;
 border: none;
 max-width: 100%;
 overflow: hidden;
 width: 1em;
 line-height: inherit;
 cursor: pointer;
 color: var(--n-text-color);
 caret-color: var(--n-caret-color);
 `),L("mirror",`
 position: absolute;
 left: 0;
 top: 0;
 white-space: pre;
 visibility: hidden;
 user-select: none;
 -webkit-user-select: none;
 opacity: 0;
 `)]),["warning","error"].map(e=>re(`${e}-status`,[L("state-border",`border: var(--n-border-${e});`),Pe("disabled",[ae("&:hover",[L("state-border",`
 box-shadow: var(--n-box-shadow-hover-${e});
 border: var(--n-border-hover-${e});
 `)]),re("active",[L("state-border",`
 box-shadow: var(--n-box-shadow-active-${e});
 border: var(--n-border-active-${e});
 `),H("base-selection-label",`background-color: var(--n-color-active-${e});`),H("base-selection-tags",`background-color: var(--n-color-active-${e});`)]),re("focus",[L("state-border",`
 box-shadow: var(--n-box-shadow-focus-${e});
 border: var(--n-border-focus-${e});
 `)])])]))]),H("base-selection-popover",`
 margin-bottom: -3px;
 display: flex;
 flex-wrap: wrap;
 margin-right: -8px;
 `),H("base-selection-tag-wrapper",`
 max-width: 100%;
 display: inline-flex;
 padding: 0 7px 3px 0;
 `,[ae("&:last-child","padding-right: 0;"),H("tag",`
 font-size: 14px;
 max-width: 100%;
 `,[L("content",`
 line-height: 1.25;
 text-overflow: ellipsis;
 overflow: hidden;
 `)])])]),kr=de({name:"InternalSelection",props:Object.assign(Object.assign({},he.props),{clsPrefix:{type:String,required:!0},bordered:{type:Boolean,default:void 0},active:Boolean,pattern:{type:String,default:""},placeholder:String,selectedOption:{type:Object,default:null},selectedOptions:{type:Array,default:null},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},multiple:Boolean,filterable:Boolean,clearable:Boolean,disabled:Boolean,size:{type:String,default:"medium"},loading:Boolean,autofocus:Boolean,showArrow:{type:Boolean,default:!0},inputProps:Object,focused:Boolean,renderTag:Function,onKeydown:Function,onClick:Function,onBlur:Function,onFocus:Function,onDeleteOption:Function,maxTagCount:[String,Number],onClear:Function,onPatternInput:Function,onPatternFocus:Function,onPatternBlur:Function,renderLabel:Function,status:String,inlineThemeDisabled:Boolean,ignoreComposition:{type:Boolean,default:!0},onResize:Function}),setup(e){const n=I(null),t=I(null),o=I(null),r=I(null),a=I(null),l=I(null),i=I(null),c=I(null),d=I(null),v=I(null),g=I(!1),_=I(!1),x=I(!1),u=he("InternalSelection","-internal-selection",xr,jn,e,se(e,"clsPrefix")),P=E(()=>e.clearable&&!e.disabled&&(x.value||e.active)),O=E(()=>e.selectedOption?e.renderTag?e.renderTag({option:e.selectedOption,handleClose:()=>{}}):e.renderLabel?e.renderLabel(e.selectedOption,!0):Be(e.selectedOption[e.labelField],e.selectedOption,!0):e.placeholder),y=E(()=>{const m=e.selectedOption;if(m)return m[e.labelField]}),F=E(()=>e.multiple?!!(Array.isArray(e.selectedOptions)&&e.selectedOptions.length):e.selectedOption!==null);function C(){var m;const{value:T}=n;if(T){const{value:Q}=t;Q&&(Q.style.width=`${T.offsetWidth}px`,e.maxTagCount!=="responsive"&&((m=d.value)===null||m===void 0||m.sync()))}}function f(){const{value:m}=v;m&&(m.style.display="none")}function w(){const{value:m}=v;m&&(m.style.display="inline-block")}Fe(se(e,"active"),m=>{m||f()}),Fe(se(e,"pattern"),()=>{e.multiple&&Te(C)});function R(m){const{onFocus:T}=e;T&&T(m)}function M(m){const{onBlur:T}=e;T&&T(m)}function N(m){const{onDeleteOption:T}=e;T&&T(m)}function D(m){const{onClear:T}=e;T&&T(m)}function A(m){const{onPatternInput:T}=e;T&&T(m)}function h(m){var T;(!m.relatedTarget||!(!((T=o.value)===null||T===void 0)&&T.contains(m.relatedTarget)))&&R(m)}function k(m){var T;!((T=o.value)===null||T===void 0)&&T.contains(m.relatedTarget)||M(m)}function B(m){D(m)}function G(){x.value=!0}function Z(){x.value=!1}function Y(m){!e.active||!e.filterable||m.target!==t.value&&m.preventDefault()}function W(m){N(m)}function X(m){if(m.key==="Backspace"&&!p.value&&!e.pattern.length){const{selectedOptions:T}=e;T!=null&&T.length&&W(T[T.length-1])}}const p=I(!1);let z=null;function q(m){const{value:T}=n;if(T){const Q=m.target.value;T.textContent=Q,C()}e.ignoreComposition&&p.value?z=m:A(m)}function le(){p.value=!0}function ue(){p.value=!1,e.ignoreComposition&&A(z),z=null}function ke(m){var T;_.value=!0,(T=e.onPatternFocus)===null||T===void 0||T.call(e,m)}function Se(m){var T;_.value=!1,(T=e.onPatternBlur)===null||T===void 0||T.call(e,m)}function ye(){var m,T;if(e.filterable)_.value=!1,(m=l.value)===null||m===void 0||m.blur(),(T=t.value)===null||T===void 0||T.blur();else if(e.multiple){const{value:Q}=r;Q==null||Q.blur()}else{const{value:Q}=a;Q==null||Q.blur()}}function me(){var m,T,Q;e.filterable?(_.value=!1,(m=l.value)===null||m===void 0||m.focus()):e.multiple?(T=r.value)===null||T===void 0||T.focus():(Q=a.value)===null||Q===void 0||Q.focus()}function ve(){const{value:m}=t;m&&(w(),m.focus())}function ce(){const{value:m}=t;m&&m.blur()}function we(m){const{value:T}=i;T&&T.setTextContent(`+${m}`)}function be(){const{value:m}=c;return m}function Ne(){return t.value}let _e=null;function Re(){_e!==null&&window.clearTimeout(_e)}function Le(){e.disabled||e.active||(Re(),_e=window.setTimeout(()=>{F.value&&(g.value=!0)},100))}function He(){Re()}function De(m){m||(Re(),g.value=!1)}Fe(F,m=>{m||(g.value=!1)}),Ee(()=>{Qt(()=>{const m=l.value;m&&(m.tabIndex=e.disabled||_.value?-1:0)})}),nn(o,e.onResize);const{inlineThemeDisabled:Ie}=e,Me=E(()=>{const{size:m}=e,{common:{cubicBezierEaseInOut:T},self:{borderRadius:Q,color:Ve,placeholderColor:lt,textColor:it,paddingSingle:at,paddingMultiple:st,caretColor:We,colorDisabled:Ue,textColorDisabled:Ge,placeholderColorDisabled:ct,colorActive:dt,boxShadowFocus:qe,boxShadowActive:Ce,boxShadowHover:s,border:S,borderFocus:$,borderHover:U,borderActive:K,arrowColor:V,arrowColorDisabled:j,loadingColor:ie,colorActiveWarning:Ye,boxShadowFocusWarning:ut,boxShadowActiveWarning:cn,boxShadowHoverWarning:dn,borderWarning:un,borderFocusWarning:fn,borderHoverWarning:hn,borderActiveWarning:vn,colorActiveError:gn,boxShadowFocusError:bn,boxShadowActiveError:pn,boxShadowHoverError:mn,borderError:yn,borderFocusError:wn,borderHoverError:Cn,borderActiveError:xn,clearColor:kn,clearColorHover:Sn,clearColorPressed:_n,clearSize:Rn,arrowSize:zn,[te("height",m)]:On,[te("fontSize",m)]:Tn}}=u.value;return{"--n-bezier":T,"--n-border":S,"--n-border-active":K,"--n-border-focus":$,"--n-border-hover":U,"--n-border-radius":Q,"--n-box-shadow-active":Ce,"--n-box-shadow-focus":qe,"--n-box-shadow-hover":s,"--n-caret-color":We,"--n-color":Ve,"--n-color-active":dt,"--n-color-disabled":Ue,"--n-font-size":Tn,"--n-height":On,"--n-padding-single":at,"--n-padding-multiple":st,"--n-placeholder-color":lt,"--n-placeholder-color-disabled":ct,"--n-text-color":it,"--n-text-color-disabled":Ge,"--n-arrow-color":V,"--n-arrow-color-disabled":j,"--n-loading-color":ie,"--n-color-active-warning":Ye,"--n-box-shadow-focus-warning":ut,"--n-box-shadow-active-warning":cn,"--n-box-shadow-hover-warning":dn,"--n-border-warning":un,"--n-border-focus-warning":fn,"--n-border-hover-warning":hn,"--n-border-active-warning":vn,"--n-color-active-error":gn,"--n-box-shadow-focus-error":bn,"--n-box-shadow-active-error":pn,"--n-box-shadow-hover-error":mn,"--n-border-error":yn,"--n-border-focus-error":wn,"--n-border-hover-error":Cn,"--n-border-active-error":xn,"--n-clear-size":Rn,"--n-clear-color":kn,"--n-clear-color-hover":Sn,"--n-clear-color-pressed":_n,"--n-arrow-size":zn}}),fe=Ie?Ae("internal-selection",E(()=>e.size[0]),Me,e):void 0;return{mergedTheme:u,mergedClearable:P,patternInputFocused:_,filterablePlaceholder:O,label:y,selected:F,showTagsPanel:g,isComposing:p,counterRef:i,counterWrapperRef:c,patternInputMirrorRef:n,patternInputRef:t,selfRef:o,multipleElRef:r,singleElRef:a,patternInputWrapperRef:l,overflowRef:d,inputTagElRef:v,handleMouseDown:Y,handleFocusin:h,handleClear:B,handleMouseEnter:G,handleMouseLeave:Z,handleDeleteOption:W,handlePatternKeyDown:X,handlePatternInputInput:q,handlePatternInputBlur:Se,handlePatternInputFocus:ke,handleMouseEnterCounter:Le,handleMouseLeaveCounter:He,handleFocusout:k,handleCompositionEnd:ue,handleCompositionStart:le,onPopoverUpdateShow:De,focus:me,focusInput:ve,blur:ye,blurInput:ce,updateCounter:we,getCounter:be,getTail:Ne,renderLabel:e.renderLabel,cssVars:Ie?void 0:Me,themeClass:fe==null?void 0:fe.themeClass,onRender:fe==null?void 0:fe.onRender}},render(){const{status:e,multiple:n,size:t,disabled:o,filterable:r,maxTagCount:a,bordered:l,clsPrefix:i,onRender:c,renderTag:d,renderLabel:v}=this;c==null||c();const g=a==="responsive",_=typeof a=="number",x=g||_,u=b(Vn,null,{default:()=>b(bo,{clsPrefix:i,loading:this.loading,showArrow:this.showArrow,showClear:this.mergedClearable&&this.selected,onClear:this.handleClear},{default:()=>{var O,y;return(y=(O=this.$slots).arrow)===null||y===void 0?void 0:y.call(O)}})});let P;if(n){const{labelField:O}=this,y=k=>b("div",{class:`${i}-base-selection-tag-wrapper`,key:k.value},d?d({option:k,handleClose:()=>{this.handleDeleteOption(k)}}):b(xt,{size:t,closable:!k.disabled,disabled:o,onClose:()=>{this.handleDeleteOption(k)},internalCloseIsButtonTag:!1,internalCloseFocusable:!1},{default:()=>v?v(k,!0):Be(k[O],k,!0)})),F=()=>(_?this.selectedOptions.slice(0,a):this.selectedOptions).map(y),C=r?b("div",{class:`${i}-base-selection-input-tag`,ref:"inputTagElRef",key:"__input-tag__"},b("input",Object.assign({},this.inputProps,{ref:"patternInputRef",tabindex:-1,disabled:o,value:this.pattern,autofocus:this.autofocus,class:`${i}-base-selection-input-tag__input`,onBlur:this.handlePatternInputBlur,onFocus:this.handlePatternInputFocus,onKeydown:this.handlePatternKeyDown,onInput:this.handlePatternInputInput,onCompositionstart:this.handleCompositionStart,onCompositionend:this.handleCompositionEnd})),b("span",{ref:"patternInputMirrorRef",class:`${i}-base-selection-input-tag__mirror`},this.pattern)):null,f=g?()=>b("div",{class:`${i}-base-selection-tag-wrapper`,ref:"counterWrapperRef"},b(xt,{size:t,ref:"counterRef",onMouseenter:this.handleMouseEnterCounter,onMouseleave:this.handleMouseLeaveCounter,disabled:o})):void 0;let w;if(_){const k=this.selectedOptions.length-a;k>0&&(w=b("div",{class:`${i}-base-selection-tag-wrapper`,key:"__counter__"},b(xt,{size:t,ref:"counterRef",onMouseenter:this.handleMouseEnterCounter,disabled:o},{default:()=>`+${k}`})))}const R=g?r?b(Kt,{ref:"overflowRef",updateCounter:this.updateCounter,getCounter:this.getCounter,getTail:this.getTail,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:F,counter:f,tail:()=>C}):b(Kt,{ref:"overflowRef",updateCounter:this.updateCounter,getCounter:this.getCounter,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:F,counter:f}):_?F().concat(w):F(),M=x?()=>b("div",{class:`${i}-base-selection-popover`},g?F():this.selectedOptions.map(y)):void 0,N=x?{show:this.showTagsPanel,trigger:"hover",overlap:!0,placement:"top",width:"trigger",onUpdateShow:this.onPopoverUpdateShow,theme:this.mergedTheme.peers.Popover,themeOverrides:this.mergedTheme.peerOverrides.Popover}:null,A=(this.selected?!1:this.active?!this.pattern&&!this.isComposing:!0)?b("div",{class:`${i}-base-selection-placeholder ${i}-base-selection-overlay`},b("div",{class:`${i}-base-selection-placeholder__inner`},this.placeholder)):null,h=r?b("div",{ref:"patternInputWrapperRef",class:`${i}-base-selection-tags`},R,g?null:C,u):b("div",{ref:"multipleElRef",class:`${i}-base-selection-tags`,tabindex:o?void 0:0},R,u);P=b(Ke,null,x?b(wo,Object.assign({},N,{scrollable:!0,style:"max-height: calc(var(--v-target-height) * 6.6);"}),{trigger:()=>h,default:M}):h,A)}else if(r){const O=this.pattern||this.isComposing,y=this.active?!O:!this.selected,F=this.active?!1:this.selected;P=b("div",{ref:"patternInputWrapperRef",class:`${i}-base-selection-label`},b("input",Object.assign({},this.inputProps,{ref:"patternInputRef",class:`${i}-base-selection-input`,value:this.active?this.pattern:"",placeholder:"",readonly:o,disabled:o,tabindex:-1,autofocus:this.autofocus,onFocus:this.handlePatternInputFocus,onBlur:this.handlePatternInputBlur,onInput:this.handlePatternInputInput,onCompositionstart:this.handleCompositionStart,onCompositionend:this.handleCompositionEnd})),F?b("div",{class:`${i}-base-selection-label__render-label ${i}-base-selection-overlay`,key:"input"},b("div",{class:`${i}-base-selection-overlay__wrapper`},d?d({option:this.selectedOption,handleClose:()=>{}}):v?v(this.selectedOption,!0):Be(this.label,this.selectedOption,!0))):null,y?b("div",{class:`${i}-base-selection-placeholder ${i}-base-selection-overlay`,key:"placeholder"},b("div",{class:`${i}-base-selection-overlay__wrapper`},this.filterablePlaceholder)):null,u)}else P=b("div",{ref:"singleElRef",class:`${i}-base-selection-label`,tabindex:this.disabled?void 0:0},this.label!==void 0?b("div",{class:`${i}-base-selection-input`,title:Po(this.label),key:"input"},b("div",{class:`${i}-base-selection-input__content`},d?d({option:this.selectedOption,handleClose:()=>{}}):v?v(this.selectedOption,!0):Be(this.label,this.selectedOption,!0))):b("div",{class:`${i}-base-selection-placeholder ${i}-base-selection-overlay`,key:"placeholder"},b("div",{class:`${i}-base-selection-placeholder__inner`},this.placeholder)),u);return b("div",{ref:"selfRef",class:[`${i}-base-selection`,this.themeClass,e&&`${i}-base-selection--${e}-status`,{[`${i}-base-selection--active`]:this.active,[`${i}-base-selection--selected`]:this.selected||this.active&&this.pattern,[`${i}-base-selection--disabled`]:this.disabled,[`${i}-base-selection--multiple`]:this.multiple,[`${i}-base-selection--focus`]:this.focused}],style:this.cssVars,onClick:this.onClick,onMouseenter:this.handleMouseEnter,onMouseleave:this.handleMouseLeave,onKeydown:this.onKeydown,onFocusin:this.handleFocusin,onFocusout:this.handleFocusout,onMousedown:this.handleMouseDown},P,l?b("div",{class:`${i}-base-selection__border`}):null,l?b("div",{class:`${i}-base-selection__state-border`}):null)}});function ot(e){return e.type==="group"}function an(e){return e.type==="ignored"}function kt(e,n){try{return!!(1+n.toString().toLowerCase().indexOf(e.trim().toLowerCase()))}catch{return!1}}function Sr(e,n){return{getIsGroup:ot,getIgnored:an,getKey(o){return ot(o)?o.name||o.key||"key-required":o[e]},getChildren(o){return o[n]}}}function _r(e,n,t,o){if(!n)return e;function r(a){if(!Array.isArray(a))return[];const l=[];for(const i of a)if(ot(i)){const c=r(i[o]);c.length&&l.push(Object.assign({},i,{[o]:c}))}else{if(an(i))continue;n(t,i)&&l.push(i)}return l}return r(e)}function Rr(e,n,t){const o=new Map;return e.forEach(r=>{ot(r)?r[t].forEach(a=>{o.set(a[n],a)}):o.set(r[n],r)}),o}const St=Wn&&"loading"in document.createElement("img"),zr=(e={})=>{var n;const{root:t=null}=e;return{hash:`${e.rootMargin||"0px 0px 0px 0px"}-${Array.isArray(e.threshold)?e.threshold.join(","):(n=e.threshold)!==null&&n!==void 0?n:"0"}`,options:Object.assign(Object.assign({},e),{root:(typeof t=="string"?document.querySelector(t):t)||document.documentElement})}},_t=new WeakMap,Rt=new WeakMap,zt=new WeakMap,Or=(e,n,t)=>{if(!e)return()=>{};const o=zr(n),{root:r}=o.options;let a;const l=_t.get(r);l?a=l:(a=new Map,_t.set(r,a));let i,c;a.has(o.hash)?(c=a.get(o.hash),c[1].has(e)||(i=c[0],c[1].add(e),i.observe(e))):(i=new IntersectionObserver(g=>{g.forEach(_=>{if(_.isIntersecting){const x=Rt.get(_.target),u=zt.get(_.target);x&&x(),u&&(u.value=!0)}})},o.options),i.observe(e),c=[i,new Set([e])],a.set(o.hash,c));let d=!1;const v=()=>{d||(Rt.delete(e),zt.delete(e),d=!0,c[1].has(e)&&(c[0].unobserve(e),c[1].delete(e)),c[1].size<=0&&a.delete(o.hash),a.size||_t.delete(r))};return Rt.set(e,v),zt.set(e,t),v},Tr=Jt("n-avatar-group"),Pr=H("avatar",`
 width: var(--n-merged-size);
 height: var(--n-merged-size);
 color: #FFF;
 font-size: var(--n-font-size);
 display: inline-flex;
 position: relative;
 overflow: hidden;
 text-align: center;
 border: var(--n-border);
 border-radius: var(--n-border-radius);
 --n-merged-color: var(--n-color);
 background-color: var(--n-merged-color);
 transition:
 border-color .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
`,[Un(ae("&","--n-merged-color: var(--n-color-modal);")),Gn(ae("&","--n-merged-color: var(--n-color-popover);")),ae("img",`
 width: 100%;
 height: 100%;
 `),L("text",`
 white-space: nowrap;
 display: inline-block;
 position: absolute;
 left: 50%;
 top: 50%;
 `),H("icon",`
 vertical-align: bottom;
 font-size: calc(var(--n-merged-size) - 6px);
 `),L("text","line-height: 1.25")]),Fr=Object.assign(Object.assign({},he.props),{size:[String,Number],src:String,circle:{type:Boolean,default:void 0},objectFit:String,round:{type:Boolean,default:void 0},bordered:{type:Boolean,default:void 0},onError:Function,fallbackSrc:String,intersectionObserverOptions:Object,lazy:Boolean,onLoad:Function,renderPlaceholder:Function,renderFallback:Function,imgProps:Object,color:String}),Ir=de({name:"Avatar",props:Fr,setup(e){const{mergedClsPrefixRef:n,inlineThemeDisabled:t}=rt(e),o=I(!1);let r=null;const a=I(null),l=I(null),i=()=>{const{value:C}=a;if(C&&(r===null||r!==C.innerHTML)){r=C.innerHTML;const{value:f}=l;if(f){const{offsetWidth:w,offsetHeight:R}=f,{offsetWidth:M,offsetHeight:N}=C,D=.9,A=Math.min(w/M*D,R/N*D,1);C.style.transform=`translateX(-50%) translateY(-50%) scale(${A})`}}},c=je(Tr,null),d=E(()=>{const{size:C}=e;if(C)return C;const{size:f}=c||{};return f||"medium"}),v=he("Avatar","-avatar",Pr,qn,e,n),g=je(ln,null),_=E(()=>{if(c)return!0;const{round:C,circle:f}=e;return C!==void 0||f!==void 0?C||f:g?g.roundRef.value:!1}),x=E(()=>c?!0:e.bordered||!1),u=C=>{var f;if(!y.value)return;o.value=!0;const{onError:w,imgProps:R}=e;(f=R==null?void 0:R.onError)===null||f===void 0||f.call(R,C),w&&w(C)};Fe(()=>e.src,()=>o.value=!1);const P=E(()=>{const C=d.value,f=_.value,w=x.value,{color:R}=e,{self:{borderRadius:M,fontSize:N,color:D,border:A,colorModal:h,colorPopover:k},common:{cubicBezierEaseInOut:B}}=v.value;let G;return typeof C=="number"?G=`${C}px`:G=v.value.self[te("height",C)],{"--n-font-size":N,"--n-border":w?A:"none","--n-border-radius":f?"50%":M,"--n-color":R||D,"--n-color-modal":R||h,"--n-color-popover":R||k,"--n-bezier":B,"--n-merged-size":`var(--n-avatar-size-override, ${G})`}}),O=t?Ae("avatar",E(()=>{const C=d.value,f=_.value,w=x.value,{color:R}=e;let M="";return C&&(typeof C=="number"?M+=`a${C}`:M+=C[0]),f&&(M+="b"),w&&(M+="c"),R&&(M+=Ft(R)),M}),P,e):void 0,y=I(!e.lazy);Ee(()=>{if(St)return;let C;const f=Qt(()=>{C==null||C(),C=void 0,e.lazy&&(C=Or(l.value,e.intersectionObserverOptions,y))});Bt(()=>{f(),C==null||C()})});const F=I(!e.lazy);return{textRef:a,selfRef:l,mergedRoundRef:_,mergedClsPrefix:n,fitTextTransform:i,cssVars:t?void 0:P,themeClass:O==null?void 0:O.themeClass,onRender:O==null?void 0:O.onRender,hasLoadError:o,handleError:u,shouldStartLoading:y,loaded:F,mergedOnLoad:C=>{var f;const{onLoad:w,imgProps:R}=e;w==null||w(C),(f=R==null?void 0:R.onLoad)===null||f===void 0||f.call(R,C),F.value=!0}}},render(){var e,n;const{$slots:t,src:o,mergedClsPrefix:r,lazy:a,onRender:l,mergedOnLoad:i,shouldStartLoading:c,loaded:d,hasLoadError:v}=this;l==null||l();let g;const _=!d&&!v&&(this.renderPlaceholder?this.renderPlaceholder():(n=(e=this.$slots).placeholder)===null||n===void 0?void 0:n.call(e));return this.hasLoadError?g=this.renderFallback?this.renderFallback():Xt(t.fallback,()=>[b("img",{src:this.fallbackSrc,style:{objectFit:this.objectFit}})]):g=et(t.default,x=>{if(x)return b(Tt,{onResize:this.fitTextTransform},{default:()=>b("span",{ref:"textRef",class:`${r}-avatar__text`},x)});if(o){const{imgProps:u}=this;return b("img",Object.assign(Object.assign({},u),{loading:St&&!this.intersectionObserverOptions&&a?"lazy":"eager",src:St||c||d?o:void 0,onLoad:i,"data-image-src":o,onError:this.handleError,style:[u==null?void 0:u.style,{objectFit:this.objectFit},_?{height:"0",width:"0",visibility:"hidden",position:"absolute"}:""]}))}}),b("span",{ref:"selfRef",class:[`${r}-avatar`,this.themeClass],style:this.cssVars},g,a&&_)}}),Mr=ae([H("select",`
 z-index: auto;
 outline: none;
 width: 100%;
 position: relative;
 `),H("select-menu",`
 margin: 4px 0;
 box-shadow: var(--n-menu-box-shadow);
 `,[Zt({originalTransition:"background-color .3s var(--n-bezier), box-shadow .3s var(--n-bezier)"})])]),Br=Object.assign(Object.assign({},he.props),{to:It.propTo,bordered:{type:Boolean,default:void 0},clearable:Boolean,clearFilterAfterSelect:{type:Boolean,default:!0},options:{type:Array,default:()=>[]},defaultValue:{type:[String,Number,Array],default:null},keyboard:{type:Boolean,default:!0},value:[String,Number,Array],placeholder:String,menuProps:Object,multiple:Boolean,size:String,filterable:Boolean,disabled:{type:Boolean,default:void 0},remote:Boolean,loading:Boolean,filter:Function,placement:{type:String,default:"bottom-start"},widthMode:{type:String,default:"trigger"},tag:Boolean,onCreate:Function,fallbackOption:{type:[Function,Boolean],default:void 0},show:{type:Boolean,default:void 0},showArrow:{type:Boolean,default:!0},maxTagCount:[Number,String],consistentMenuWidth:{type:Boolean,default:!0},virtualScroll:{type:Boolean,default:!0},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},childrenField:{type:String,default:"children"},renderLabel:Function,renderOption:Function,renderTag:Function,"onUpdate:value":[Function,Array],inputProps:Object,nodeProps:Function,ignoreComposition:{type:Boolean,default:!0},showOnFocus:Boolean,onUpdateValue:[Function,Array],onBlur:[Function,Array],onClear:[Function,Array],onFocus:[Function,Array],onScroll:[Function,Array],onSearch:[Function,Array],onUpdateShow:[Function,Array],"onUpdate:show":[Function,Array],displayDirective:{type:String,default:"show"},resetMenuOnOptionsChange:{type:Boolean,default:!0},status:String,showCheckmark:{type:Boolean,default:!0},onChange:[Function,Array],items:Array}),$r=de({name:"Select",props:Br,setup(e){const{mergedClsPrefixRef:n,mergedBorderedRef:t,namespaceRef:o,inlineThemeDisabled:r}=rt(e),a=he("Select","-select",Mr,Qn,e,n),l=I(e.defaultValue),i=se(e,"value"),c=Lt(i,l),d=I(!1),v=I(""),g=E(()=>{const{valueField:s,childrenField:S}=e,$=Sr(s,S);return cr(k.value,$)}),_=E(()=>Rr(A.value,e.valueField,e.childrenField)),x=I(!1),u=Lt(se(e,"show"),x),P=I(null),O=I(null),y=I(null),{localeRef:F}=en("Select"),C=E(()=>{var s;return(s=e.placeholder)!==null&&s!==void 0?s:F.value.placeholder}),f=Co(e,["items","options"]),w=[],R=I([]),M=I([]),N=I(new Map),D=E(()=>{const{fallbackOption:s}=e;if(s===void 0){const{labelField:S,valueField:$}=e;return U=>({[S]:String(U),[$]:U})}return s===!1?!1:S=>Object.assign(s(S),{value:S})}),A=E(()=>M.value.concat(R.value).concat(f.value)),h=E(()=>{const{filter:s}=e;if(s)return s;const{labelField:S,valueField:$}=e;return(U,K)=>{if(!K)return!1;const V=K[S];if(typeof V=="string")return kt(U,V);const j=K[$];return typeof j=="string"?kt(U,j):typeof j=="number"?kt(U,String(j)):!1}}),k=E(()=>{if(e.remote)return f.value;{const{value:s}=A,{value:S}=v;return!S.length||!e.filterable?s:_r(s,h.value,S,e.childrenField)}});function B(s){const S=e.remote,{value:$}=N,{value:U}=_,{value:K}=D,V=[];return s.forEach(j=>{if(U.has(j))V.push(U.get(j));else if(S&&$.has(j))V.push($.get(j));else if(K){const ie=K(j);ie&&V.push(ie)}}),V}const G=E(()=>{if(e.multiple){const{value:s}=c;return Array.isArray(s)?B(s):[]}return null}),Z=E(()=>{const{value:s}=c;return!e.multiple&&!Array.isArray(s)?s===null?null:B([s])[0]||null:null}),Y=Yn(e),{mergedSizeRef:W,mergedDisabledRef:X,mergedStatusRef:p}=Y;function z(s,S){const{onChange:$,"onUpdate:value":U,onUpdateValue:K}=e,{nTriggerFormChange:V,nTriggerFormInput:j}=Y;$&&ge($,s,S),K&&ge(K,s,S),U&&ge(U,s,S),l.value=s,V(),j()}function q(s){const{onBlur:S}=e,{nTriggerFormBlur:$}=Y;S&&ge(S,s),$()}function le(){const{onClear:s}=e;s&&ge(s)}function ue(s){const{onFocus:S,showOnFocus:$}=e,{nTriggerFormFocus:U}=Y;S&&ge(S,s),U(),$&&ve()}function ke(s){const{onSearch:S}=e;S&&ge(S,s)}function Se(s){const{onScroll:S}=e;S&&ge(S,s)}function ye(){var s;const{remote:S,multiple:$}=e;if(S){const{value:U}=N;if($){const{valueField:K}=e;(s=G.value)===null||s===void 0||s.forEach(V=>{U.set(V[K],V)})}else{const K=Z.value;K&&U.set(K[e.valueField],K)}}}function me(s){const{onUpdateShow:S,"onUpdate:show":$}=e;S&&ge(S,s),$&&ge($,s),x.value=s}function ve(){X.value||(me(!0),x.value=!0,e.filterable&&Ge())}function ce(){me(!1)}function we(){v.value="",M.value=w}const be=I(!1);function Ne(){e.filterable&&(be.value=!0)}function _e(){e.filterable&&(be.value=!1,u.value||we())}function Re(){X.value||(u.value?e.filterable?Ge():ce():ve())}function Le(s){var S,$;!(($=(S=y.value)===null||S===void 0?void 0:S.selfRef)===null||$===void 0)&&$.contains(s.relatedTarget)||(d.value=!1,q(s),ce())}function He(s){ue(s),d.value=!0}function De(s){d.value=!0}function Ie(s){var S;!((S=P.value)===null||S===void 0)&&S.$el.contains(s.relatedTarget)||(d.value=!1,q(s),ce())}function Me(){var s;(s=P.value)===null||s===void 0||s.focus(),ce()}function fe(s){var S;u.value&&(!((S=P.value)===null||S===void 0)&&S.$el.contains(eo(s))||ce())}function m(s){if(!Array.isArray(s))return[];if(D.value)return Array.from(s);{const{remote:S}=e,{value:$}=_;if(S){const{value:U}=N;return s.filter(K=>$.has(K)||U.has(K))}else return s.filter(U=>$.has(U))}}function T(s){Q(s.rawNode)}function Q(s){if(X.value)return;const{tag:S,remote:$,clearFilterAfterSelect:U,valueField:K}=e;if(S&&!$){const{value:V}=M,j=V[0]||null;if(j){const ie=R.value;ie.length?ie.push(j):R.value=[j],M.value=w}}if($&&N.value.set(s[K],s),e.multiple){const V=m(c.value),j=V.findIndex(ie=>ie===s[K]);if(~j){if(V.splice(j,1),S&&!$){const ie=Ve(s[K]);~ie&&(R.value.splice(ie,1),U&&(v.value=""))}}else V.push(s[K]),U&&(v.value="");z(V,B(V))}else{if(S&&!$){const V=Ve(s[K]);~V?R.value=[R.value[V]]:R.value=w}Ue(),ce(),z(s[K],s)}}function Ve(s){return R.value.findIndex($=>$[e.valueField]===s)}function lt(s){u.value||ve();const{value:S}=s.target;v.value=S;const{tag:$,remote:U}=e;if(ke(S),$&&!U){if(!S){M.value=w;return}const{onCreate:K}=e,V=K?K(S):{[e.labelField]:S,[e.valueField]:S},{valueField:j}=e;f.value.some(ie=>ie[j]===V[j])||R.value.some(ie=>ie[j]===V[j])?M.value=w:M.value=[V]}}function it(s){s.stopPropagation();const{multiple:S}=e;!S&&e.filterable&&ce(),le(),S?z([],[]):z(null,null)}function at(s){!tt(s,"action")&&!tt(s,"empty")&&s.preventDefault()}function st(s){Se(s)}function We(s){var S,$,U,K,V;if(!e.keyboard){s.preventDefault();return}switch(s.key){case" ":if(e.filterable)break;s.preventDefault();case"Enter":if(!(!((S=P.value)===null||S===void 0)&&S.isComposing)){if(u.value){const j=($=y.value)===null||$===void 0?void 0:$.getPendingTmNode();j?T(j):e.filterable||(ce(),Ue())}else if(ve(),e.tag&&be.value){const j=M.value[0];if(j){const ie=j[e.valueField],{value:Ye}=c;e.multiple&&Array.isArray(Ye)&&Ye.some(ut=>ut===ie)||Q(j)}}}s.preventDefault();break;case"ArrowUp":if(s.preventDefault(),e.loading)return;u.value&&((U=y.value)===null||U===void 0||U.prev());break;case"ArrowDown":if(s.preventDefault(),e.loading)return;u.value?(K=y.value)===null||K===void 0||K.next():ve();break;case"Escape":u.value&&(to(s),ce()),(V=P.value)===null||V===void 0||V.focus();break}}function Ue(){var s;(s=P.value)===null||s===void 0||s.focus()}function Ge(){var s;(s=P.value)===null||s===void 0||s.focusInput()}function ct(){var s;u.value&&((s=O.value)===null||s===void 0||s.syncPosition())}ye(),Fe(se(e,"options"),ye);const dt={focus:()=>{var s;(s=P.value)===null||s===void 0||s.focus()},blur:()=>{var s;(s=P.value)===null||s===void 0||s.blur()}},qe=E(()=>{const{self:{menuBoxShadow:s}}=a.value;return{"--n-menu-box-shadow":s}}),Ce=r?Ae("select",void 0,qe,e):void 0;return Object.assign(Object.assign({},dt),{mergedStatus:p,mergedClsPrefix:n,mergedBordered:t,namespace:o,treeMate:g,isMounted:Zn(),triggerRef:P,menuRef:y,pattern:v,uncontrolledShow:x,mergedShow:u,adjustedTo:It(e),uncontrolledValue:l,mergedValue:c,followerRef:O,localizedPlaceholder:C,selectedOption:Z,selectedOptions:G,mergedSize:W,mergedDisabled:X,focused:d,activeWithoutMenuOpen:be,inlineThemeDisabled:r,onTriggerInputFocus:Ne,onTriggerInputBlur:_e,handleTriggerOrMenuResize:ct,handleMenuFocus:De,handleMenuBlur:Ie,handleMenuTabOut:Me,handleTriggerClick:Re,handleToggle:T,handleDeleteOption:Q,handlePatternInput:lt,handleClear:it,handleTriggerBlur:Le,handleTriggerFocus:He,handleKeydown:We,handleMenuAfterLeave:we,handleMenuClickOutside:fe,handleMenuScroll:st,handleMenuKeydown:We,handleMenuMousedown:at,mergedTheme:a,cssVars:r?void 0:qe,themeClass:Ce==null?void 0:Ce.themeClass,onRender:Ce==null?void 0:Ce.onRender})},render(){return b("div",{class:`${this.mergedClsPrefix}-select`},b(xo,null,{default:()=>[b(ko,null,{default:()=>b(kr,{ref:"triggerRef",inlineThemeDisabled:this.inlineThemeDisabled,status:this.mergedStatus,inputProps:this.inputProps,clsPrefix:this.mergedClsPrefix,showArrow:this.showArrow,maxTagCount:this.maxTagCount,bordered:this.mergedBordered,active:this.activeWithoutMenuOpen||this.mergedShow,pattern:this.pattern,placeholder:this.localizedPlaceholder,selectedOption:this.selectedOption,selectedOptions:this.selectedOptions,multiple:this.multiple,renderTag:this.renderTag,renderLabel:this.renderLabel,filterable:this.filterable,clearable:this.clearable,disabled:this.mergedDisabled,size:this.mergedSize,theme:this.mergedTheme.peers.InternalSelection,labelField:this.labelField,valueField:this.valueField,themeOverrides:this.mergedTheme.peerOverrides.InternalSelection,loading:this.loading,focused:this.focused,onClick:this.handleTriggerClick,onDeleteOption:this.handleDeleteOption,onPatternInput:this.handlePatternInput,onClear:this.handleClear,onBlur:this.handleTriggerBlur,onFocus:this.handleTriggerFocus,onKeydown:this.handleKeydown,onPatternBlur:this.onTriggerInputBlur,onPatternFocus:this.onTriggerInputFocus,onResize:this.handleTriggerOrMenuResize,ignoreComposition:this.ignoreComposition},{arrow:()=>{var e,n;return[(n=(e=this.$slots).arrow)===null||n===void 0?void 0:n.call(e)]}})}),b(So,{ref:"followerRef",show:this.mergedShow,to:this.adjustedTo,teleportDisabled:this.adjustedTo===It.tdkey,containerClass:this.namespace,width:this.consistentMenuWidth?"target":void 0,minWidth:"target",placement:this.placement},{default:()=>b(Yt,{name:"fade-in-scale-up-transition",appear:this.isMounted,onAfterLeave:this.handleMenuAfterLeave},{default:()=>{var e,n,t;return this.mergedShow||this.displayDirective==="show"?((e=this.onRender)===null||e===void 0||e.call(this),Xn(b(gr,Object.assign({},this.menuProps,{ref:"menuRef",onResize:this.handleTriggerOrMenuResize,inlineThemeDisabled:this.inlineThemeDisabled,virtualScroll:this.consistentMenuWidth&&this.virtualScroll,class:[`${this.mergedClsPrefix}-select-menu`,this.themeClass,(n=this.menuProps)===null||n===void 0?void 0:n.class],clsPrefix:this.mergedClsPrefix,focusable:!0,labelField:this.labelField,valueField:this.valueField,autoPending:!0,nodeProps:this.nodeProps,theme:this.mergedTheme.peers.InternalSelectMenu,themeOverrides:this.mergedTheme.peerOverrides.InternalSelectMenu,treeMate:this.treeMate,multiple:this.multiple,size:"medium",renderOption:this.renderOption,renderLabel:this.renderLabel,value:this.mergedValue,style:[(t=this.menuProps)===null||t===void 0?void 0:t.style,this.cssVars],onToggle:this.handleToggle,onScroll:this.handleMenuScroll,onFocus:this.handleMenuFocus,onBlur:this.handleMenuBlur,onKeydown:this.handleMenuKeydown,onTabOut:this.handleMenuTabOut,onMousedown:this.handleMenuMousedown,show:this.mergedShow,showCheckmark:this.showCheckmark,resetMenuOnOptionsChange:this.resetMenuOnOptionsChange}),{empty:()=>{var o,r;return[(r=(o=this.$slots).empty)===null||r===void 0?void 0:r.call(o)]},action:()=>{var o,r;return[(r=(o=this.$slots).action)===null||r===void 0?void 0:r.call(o)]}}),this.displayDirective==="show"?[[Jn,this.mergedShow],[Nt,this.handleMenuClickOutside,void 0,{capture:!0}]]:[[Nt,this.handleMenuClickOutside,void 0,{capture:!0}]])):null}})})]}))}});const sn=e=>(ao("data-v-d25498fe"),e=e(),so(),e),Er=sn(()=>$e("br",null,null,-1)),Ar=sn(()=>$e("br",null,null,-1)),Nr={id:"addlist"},Lr={key:0,id:"musicshowdiv"},Hr={key:1,id:"albumshowdiv"},Dr={key:2,id:"artistshowdiv"},Kr={class:"page"},jr={__name:"Search",setup(e){let n=I("kw"),t=I("music"),o=I(""),r=I(20),a=I(1),l=I([]),i=I([{label:"某我",value:"kw"},{label:"鹅厂",value:"qq"},{label:"10086",value:"mg",disabled:!0}]),c=I([{label:"单曲",value:"music"},{label:"专辑",value:"album"},{label:"歌手（全部专辑）",value:"artist"},{label:"歌手（全部歌曲）-不分失效",value:"artistAllSong"}]),d=(O,y)=>{a.value=1,l.value=[]};oo(()=>{co().then(O=>{Te(()=>{i.value=O.data.data})})});let v=()=>{uo(n.value,t.value,o.value,r.value,a.value).then(O=>{O.data.code===200&&Te(()=>{l.value=O.data.data.records})})},g=O=>{fo(O,n.value,2e3).then(y=>{y.data.code===200?window.$message.success("提交成功加入待下载"):window.$message.error("提交失败："+y.data.msg)})},_=O=>{ho(O,n.value,2e3).then(y=>{y.data.code===200?window.$message.success("提交成功加入待下载"):window.$message.error("提交失败："+y.data.msg)})},x=O=>{vo(O,n.value,2e3).then(y=>{y.data.code===200?window.$message.success("提交成功加入待下载"):window.$message.error("提交失败："+y.data.msg)})},u=()=>{Te(()=>{a.value=a.value+1,v()})},P=()=>{Te(()=>{a.value=a.value-1,a.value<1&&(a.value=1,window.$message.error("第一页就别点了吧")),v()})};return(O,y)=>{const F=$r,C=po,f=io,w=To,R=Ir,M=Ro,N=zo,D=Oo,A=_o;return pe(),ze(Ke,null,[ne(go),ne(w,{inline:"",onKeyup:ro(ee(v),["enter","native"])},{default:oe(()=>[ne(F,{value:ee(n),"onUpdate:value":[y[0]||(y[0]=h=>gt(n)?n.value=h:n=h),ee(d)],placeholder:"Select",options:ee(i)},null,8,["value","options","onUpdate:value"]),ne(F,{value:ee(t),"onUpdate:value":[y[1]||(y[1]=h=>gt(t)?t.value=h:t=h),ee(d)],placeholder:"Select",options:ee(c)},null,8,["value","options","onUpdate:value"]),ne(C,{placeholder:"关键字",value:ee(o),"onUpdate:value":y[2]||(y[2]=h=>gt(o)?o.value=h:o=h)},null,8,["value"]),ne(f,{"attr-type":"button",onClick:ee(v)},{default:oe(()=>[xe(" 搜索 ")]),_:1},8,["onClick"])]),_:1},8,["onKeyup"]),Er,Ar,$e("div",Nr,[ee(t)==="music"?(pe(),ze("div",Lr,[(pe(!0),ze(Ke,null,ht(ee(l),(h,k)=>(pe(),bt(D,null,{default:oe(()=>[ne(N,null,{prefix:oe(()=>[ne(R,{size:48,src:h.pic},null,8,["src"])]),suffix:oe(()=>[ne(f,{onClick:B=>ee(g)(h.id)},{default:oe(()=>[xe(" 下载")]),_:2},1032,["onClick"])]),default:oe(()=>[ne(M,{title:h.name,description:h.artistName},null,8,["title","description"])]),_:2},1024)]),_:2},1024))),256))])):vt("",!0),ee(t)==="album"?(pe(),ze("div",Hr,[(pe(!0),ze(Ke,null,ht(ee(l),(h,k)=>(pe(),bt(D,null,{default:oe(()=>[ne(N,null,{prefix:oe(()=>[ne(R,{size:48,src:h.pic},null,8,["src"])]),suffix:oe(()=>[ne(f,{onClick:B=>ee(x)(h.albumid)},{default:oe(()=>[xe(" 下载该专辑全部歌曲")]),_:2},1032,["onClick"])]),default:oe(()=>[ne(M,{title:h.albumName,description:h.artistName},null,8,["title","description"])]),_:2},1024)]),_:2},1024))),256))])):vt("",!0),ee(t)==="artist"||ee(t)==="artistAllSong"?(pe(),ze("div",Dr,[(pe(!0),ze(Ke,null,ht(ee(l),(h,k)=>(pe(),bt(D,null,{default:oe(()=>[ne(N,null,{prefix:oe(()=>[ne(R,{size:48,src:h.pic},null,8,["src"])]),suffix:oe(()=>[ne(f,{onClick:B=>ee(_)(h.artistid)},{default:oe(()=>[xe(" 下载全部专辑歌曲")]),_:2},1032,["onClick"]),ne(A,{"show-arrow":!1,trigger:"hover"},{trigger:oe(()=>[ne(f,{onClick:B=>ee(_)(h.artistid)},{default:oe(()=>[xe(" 下载全部歌曲")]),_:2},1032,["onClick"])]),default:oe(()=>[xe(" 部分歌曲不在专辑中此功能也可下载（部分不支持比如鹅厂） ")]),_:2},1024)]),default:oe(()=>[ne(M,{title:h.artistName,description:h.total+"张专辑"},null,8,["title","description"])]),_:2},1024)]),_:2},1024))),256))])):vt("",!0),$e("div",Kr,[ne(f,{onClick:ee(P)},{default:oe(()=>[xe("上一页")]),_:1},8,["onClick"]),$e("div",null,[$e("h3",null,"   当前第： "+lo(ee(a))+"页   ",1)]),ne(f,{onClick:ee(u)},{default:oe(()=>[xe("下一页")]),_:1},8,["onClick"])])])],64)}}},Qr=no(jr,[["__scopeId","data-v-d25498fe"]]);export{Qr as default};
