package org.simplecoin;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey reciepient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash(){
        sequence++;
        return StringUtil.applySha256(
            StringUtil.getStringFromKey(sender) +
                   StringUtil.getStringFromKey(reciepient) +
                   Float.toString(value) +
                   sequence
                   );
    }

    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction(){
        if(verifySignature() == false){
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        // gather transaction inputs (make sure they are unspent)
        for(TransactionInput i: inputs){
            i.UTXO = SimpleChain.UTXOs.get(i.transactionOutputId);
        }

        // check if transaction is valid
        if(getInputsValue() < SimpleChain.minimumTransaction){
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        // generate transaction outputs:
        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); // send value to reciepient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // send the left over 'change' back to sender

        // add outputs to Unspent list
        for(TransactionOutput o: outputs){
            SimpleChain.UTXOs.put(o.id, o);
        }
        // remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i: inputs){
            if(i.UTXO == null) continue;
            SimpleChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    // returns sum of inputs values
    public float getInputsValue(){
        float total = 0;
        for(TransactionInput i: inputs){
            if(i.UTXO == null) continue; // if transaction can't be found skip it.
            total += i.UTXO.value;
        }
        return total;
    }

    // return sum of outputs
    public float getOutputsValue(){
        float total = 0;
        for(TransactionOutput o: outputs){
            total += o.value;
        }
        return total;
    }
}
