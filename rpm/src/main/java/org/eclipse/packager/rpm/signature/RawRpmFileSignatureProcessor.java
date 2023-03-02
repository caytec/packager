package org.eclipse.packager.rpm.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.eclipse.packager.rpm.RpmLead;
import org.eclipse.packager.rpm.RpmSignatureTag;
import org.eclipse.packager.rpm.RpmTag;
import org.eclipse.packager.rpm.header.Header;
import org.eclipse.packager.rpm.parse.InputHeader;
import org.eclipse.packager.rpm.parse.RpmInputStream;

public class RawRpmFileSignatureProcessor implements SignatureProcessor {

    private RpmLead lead;

    private InputHeader<RpmTag> payloadHeader;

    private Header<RpmSignatureTag> signatureHeader;

    private CpioArchiveOutputStream cpioArchiveOutputStream;

    public void perform(RpmInputStream input, PGPKeyPair pgpKeyPair) throws IOException {
        input.available();
        this.lead = input.getLead();
        this.payloadHeader = input.getPayloadHeader();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(input.getCpioStream(), out);
        this.cpioArchiveOutputStream = new CpioArchiveOutputStream(out);

        byte[] headerBytes = new byte[] {};
        input.read(headerBytes, (int) payloadHeader.getStart(), (int) payloadHeader.getLength());
        ByteBuffer header = ByteBuffer.allocate((int) payloadHeader.getLength());
        ReadableByteChannel headerChannel = Channels.newChannel(new ByteArrayInputStream(headerBytes));
        IOUtils.readFully(headerChannel, header);

        ByteBuffer data = ByteBuffer.allocate(input.getCpioStream().available());
        ReadableByteChannel payloadChannel = Channels.newChannel(new ByteArrayInputStream(headerBytes));
        IOUtils.readFully(payloadChannel, data);

        feedHeader(header);
        feedPayloadData(data);
        this.signatureHeader = new Header<>();
        finish(signatureHeader);
    }

    @Override
    public void feedHeader(ByteBuffer header) {
        // TODO Auto-generated method stub

    }

    @Override
    public void feedPayloadData(ByteBuffer data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void finish(Header<RpmSignatureTag> signature) {
        // TODO Auto-generated method stub

    }

}
