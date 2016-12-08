package au.id.villar.bytecode.parser;

import java.io.IOException;
import java.io.InputStream;

class RestrictedInputStream extends InputStream {

	private InputStream wrapped;
	private int remaining;

	RestrictedInputStream(int size, InputStream wrapped) {
		reuse(size, wrapped);
	}

	InputStream reuse(int size, InputStream wrapped) {
		this.remaining = size;
		this.wrapped = wrapped;
		return this;
	}

	void consumeRemaining() throws IOException {
		long actual = wrapped.skip(remaining);
		if(actual < remaining) {
			throw new IOException("Unexpected end of stream");
		}
	}

	@Override
	public int read() throws IOException {
		if(remaining == 0) {
			return -1;
		}
		remaining--;
		return wrapped.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if(len == 0) {
			return 0;
		}
		if(remaining == 0) {
			return -1;
		}
		if(len > remaining) {
			len = remaining;
		}
		int actual = wrapped.read(b, off, len);
		if(actual == -1) {
			return -1;
		}
		remaining -= actual;
		return actual;
	}

	@Override
	public int available() throws IOException {
		int available = this.wrapped.available();
		if(available > remaining) available = remaining;
		return available;
	}

}
