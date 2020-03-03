mov r0, #0
swi 0x6c
mov r3, r0
mov r4, #0
mov r5, #0
mov r6, #0
loop:
		mov r0, #0
		swi 0x6c
		mov r5, r0      
        add r6, r6, r5 
        add r4, r4, #1
        cmp r4, r3
ble loop
mov r0, #1
mov r1, r6
swi 0x6b
swi 0x11


