mov r0, #0
swi 0x6c
mov r2, r0    @the nth
mov r3, #0	  @a
mov r4, #1    @b
mov r5, #2	  @loop

cmp r2, #1
ble a1
loop:   

        mov r6, r3
        add r3, r3, r4
        mov r4, r6 
        add r5, r5, #1
        cmp r5, r2     
       	ble loop

a1:
mov r4, r3
mov r9, r4
ldr    r2,=0xcccccccd
umull  r0,r1,r3,r2
mov    r0,r1,lsr #3
mov r3, r0
ldr    r2,=0xcccccccd
umull  r0,r1,r3,r2
mov    r0,r1,lsr #3
mov r3, r0
ldr    r2,=0xcccccccd
umull  r0,r1,r3,r2
mov    r0,r1,lsr #3
@r0 r1r2 r3 r5r6r7r8r9
cmp r0, #0
ble loop1
		mov r3, r9  						@mov the original no. to r3
		ldr    r2,=0xcccccccd
		umull  r0,r1,r3,r2
		mov    r0,r1,lsr #3
		mov r3, r0 @ mov ro to r3 			@store the new 3 digit no. in r0 an dtransfer it to r3 to further use
		mov r5, r3 @3 digit number
		mov r6, #10
		mul r3, r5, r6
		mov r6, r4
		sub r3, r6, r3
		mov r6, #1000
		mul r7, r3, r6


		mov r4, r0             				@retrieve the new original number

		mov r3, r4   						@mov the original no. to r3
		ldr    r2,=0xcccccccd
		umull  r0,r1,r3,r2
		mov    r0,r1,lsr #3
		mov r3, r0 @ mov ro to r3 			@store the new 2 digit no. in r0 an dtransfer it to r3 to further use
		mov r5, r3 @2 digit number
		mov r6, #10
		mul r3, r5, r6
		mov r6, r4
		sub r3, r6, r3
		mov r6, #100
		mov r5, r7
		mul r7, r3, r6
		add r7, r7, r5



		mov r4, r0             				@retrieve the new original number

		mov r3, r4   						@mov the original no. to r3
		ldr    r2,=0xcccccccd
		umull  r0,r1,r3,r2
		mov    r0,r1,lsr #3
		mov r3, r0 @ mov ro to r3 			@store the new 1 digit no. in r0 an dtransfer it to r3 to further use
		mov r5, r3 @2 digit number
		mov r6, #10
		mul r3, r5, r6
		mov r6, r4
		sub r3, r6, r3
		mov r6, #10
		mov r5, r7
		mul r7, r3, r6
		add r7, r7, r5




		mov r4, r0             				@retrieve the new original number

		mov r3, r4   						@mov the original no. to r3
		ldr    r2,=0xcccccccd
		umull  r0,r1,r3,r2
		mov    r0,r1,lsr #3
		mov r3, r0 @ mov ro to r3 			@store the new 2 digit no. in r0 an dtransfer it to r3 to further use
		mov r5, r3 @2 digit number
		mov r6, #10
		mul r3, r5, r6
		mov r6, r4
		sub r3, r6, r3
		mov r6, #1
		mov r5, r7
		mul r7, r3, r6
		add r7, r7, r5
		mov r0, #1
		mov r1, r7
		swi 0x6b
		b end_if

loop1:
	mov r3, r9
	ldr    r2,=0xcccccccd
	umull  r0,r1,r3,r2
	mov    r0,r1,lsr #3
	mov r3,r0
	ldr    r2,=0xcccccccd
	umull  r0,r1,r3,r2
	mov    r0,r1,lsr #3
	mov r3,r0
	cmp r0, #0
	ble loop2
			mov r4, r9            				@retrieve the new original number r4 =326
			mov r3, r4   						@mov the original no. to r3  =  326				ldr    r2,=0xcccccccd
			umull  r0,r1,r3,r2
			mov    r0,r1,lsr #3					@r0=32
			mov r3, r0 @ mov ro to r3 			@store the new 2 digit no. in r0 an dtransfer it to r3 to further use r3=  32
			mov r5, r3 @2 digit number          @r5 =32
			mov r6, #10							@r6=10
			mul r3, r5, r6 						@r3=320
			mov r6, r4 							@r6=326
			sub r3, r6, r3 						@r3=6
			mov r6, #100 						@r6=100
			
			mul r7, r3, r6   					@r7=600

			mov r4, r0             				@retrieve the new original number r4=32

			mov r3, r4   						@mov the original no. to r3=32
			ldr    r2,=0xcccccccd
			umull  r0,r1,r3,r2
			mov    r0,r1,lsr #3					@r0=3
			mov r3, r0 @ mov ro to r3 			@store the new 1 digit no. in r0 an dtransfer it to r3 to further use@r3=3
			mov r5, r3 @2 digit number			@r5=3
			mov r6, #10 						@r6=10
			mul r3, r5, r6 						@r3=30
			mov r6, r4 							@r6=32
			sub r3, r6, r3 						@r3=2
			mov r6, #10 						@r6=10
			mov r5, r7 							@r5=600
			mul r7, r3, r6  					@r7=20
			add r7, r7, r5  					@620


			add r7, r7, r0
			mov r0, #1
			mov r1, r7
			swi 0x6b
			b end_if



loop2:
				
	mov r3, r9
	ldr    r2,=0xcccccccd
	umull  r0,r1,r3,r2
	mov    r0,r1,lsr #3
	cmp r0, #0
	ble  loop3
	mov r4, r9            				@retrieve the new original number r4 =32

	mov r3, r4   						@mov the original no. to r3  =  32
	ldr    r2,=0xcccccccd
	umull  r0,r1,r3,r2
	mov    r0,r1,lsr #3					@r0=3
	mov r3, r0 @ mov ro to r3 			@store the new 2 digit no. in r0 an dtransfer it to r3 to further use r3=  3
	mov r5, r3 @2 digit number          @r5 =3
	mov r6, #10							@r6=10
	mul r3, r5, r6 						@r3=30
	mov r6, r4 							@r6=32
	sub r3, r6, r3 						@r3=2
	mov r6, #10							@r6=10
	
	mul r7, r3, r6   					@r7=20

	add r7, r7, r0
	mov r0, #1
	mov r1, r7
	swi 0x6b
	b end_if

	loop3:	
	mov r0, #1
	mov r1, r9
	swi 0x6b
	b end_if
						


end_if:
swi 0x11



