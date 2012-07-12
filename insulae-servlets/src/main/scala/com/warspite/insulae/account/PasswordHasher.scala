package com.warspite.insulae.account
import java.security.MessageDigest

object PasswordHasher {
  val md = MessageDigest.getInstance("MD5");
	def hash(in: String) = {
	  val inBytes = in.getBytes("UTF-8");
	  val outBytes = md.digest(inBytes);
	  
	  new String(outBytes);
	}
}
