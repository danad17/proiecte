t=David(:,1);
u=David(:,2);
y2=David(:,4);

figure
plot(t,u,t,y2) %, hold on
%%
iuM = 134;%153;
iyM = 136;%155;
ium = 144;%162;
iym = 145;%164;
%%
dt = t(2)-t(1);
i1 = 15;
i2 = 413;
idx = [i1:i2]
%d_id = iddata(y2,u,dt)
d_id2 = iddata(y2(idx),u(idx),dt)
%%
M_arx=arx(d_id2,[2,2,1])
Hz=tf(M_arx.B,M_arx.A,dt)
figure
resid(d_id2,M_arx)
figure
compare(d_id2,M_arx)
%%
M_armax = armax(d_id2,[2,2,2,1])
figure
resid(d_id2,M_armax)
figure
compare(d_id2,M_armax)

Hz_armax=tf(M_armax.B,M_armax.A,dt)

%Hd_armax = tf(M_armax)
Hc_armax = d2c(Hd_armax, 'zoh')
%%
M_oe2 = oe(d_id2,[2,2,1])
resid(d_id2,M_oe2,5)
figure
compare(d_id2,M_oe2)

Hz_oe2=tf(M_oe2.B,M_oe.F,dt)

% Hdz_oe = tf(M_oe)
 Hc_oe2 = d2c(Hz_oe2, 'zoh')
%%
M_iv41 = iv4(d_id2,[2,2,1])
figure
resid(d_id2,M_iv41,5)
figure
compare(d_id2,M_iv41)
% %%
% M_pem = pem(d_id2,2)
% figure
% resid(d_id2,M_pem)
% figure
% compare(d_id2,M_pem)
% %%
% M4 = n4sid(d_id,1:10)
% figure
% resid(d_id,M4)
% figure
% compare(d_id,M4)